package com.wisetime.wisetime.service.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.punch.PunchDTO;
import com.wisetime.wisetime.DTO.punch.TemporaryPunchDTO;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.DTO.request.RequestFilterDTO;
import com.wisetime.wisetime.DTO.request.RequestResponseDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.request.RequestStatusEnum;
import com.wisetime.wisetime.models.request.RequestTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.request.RequestRepository;
import com.wisetime.wisetime.service.punch.PunchLogService;
import com.wisetime.wisetime.service.punch.TemporaryPunchService;
import com.wisetime.wisetime.service.user.UserService;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TemporaryPunchService temporaryPunchService;
    
    @Autowired
    private PunchLogService punchLogService;

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Optional<Request> findById(Long requestId) {
        return requestRepository.findById(requestId);
    }

    public List<Request> findByOrganizationId(Long organizationId) {
        return requestRepository.findByOrganizationId(organizationId);
    }
    
    public Long countByTeamIdAndStatus(Long teamId, RequestStatusEnum status) {
        return requestRepository.countByTeamIdAndStatus(teamId, status);
    }

	public Request create(RequestDTO requestDTO) {
		User user = userService.findEntityById(requestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Request request = new Request();
        request.setUser(user);  
        request.setOrganization(user.getTeam().getOrganization());
        request.setRequestType(RequestTypeEnum.valueOf(requestDTO.getRequestType()));
        request.setJustification(requestDTO.getJustification());
        request.setStatus(RequestStatusEnum.PENDENTE);

        if (requestDTO.getRequestType().equals("ADICAO_DE_PONTO")) {
            List<TemporaryPunch> temporaryPunches = requestDTO.getPunches().stream().map(punchDTO -> {
                TemporaryPunch tempPunch = new TemporaryPunch();
                tempPunch.setTimestamp(LocalDateTime.parse(punchDTO.getHours(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                tempPunch.setType(punchDTO.getStatus().equals("Entrada") ? PunchTypeEnum.ENTRY : PunchTypeEnum.EXIT);
                tempPunch.setUser(user); 
                tempPunch.setRequest(request); 
                return tempPunch;
            }).collect(Collectors.toList());

            request.setTemporaryPunches(temporaryPunches);
        }

        Request savedRequest = this.save(request); 
        temporaryPunchService.saveAll(request.getTemporaryPunches());
        
        return savedRequest;
		
	}
	
	public RequestResponseDTO mapToDTO(Request request) {
	    RequestResponseDTO responseDTO = new RequestResponseDTO();
	    responseDTO.setId(request.getId());
	    responseDTO.setRequestType(request.getRequestType().name());
	    responseDTO.setJustification(request.getJustification());
	    responseDTO.setStatus(request.getStatus().name());
	    
	    List<TemporaryPunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(punch -> {
	        TemporaryPunchDTO punchDTO = new TemporaryPunchDTO();
	        punchDTO.setHours(punch.getTimestamp().toString()); 
	        punchDTO.setStatus(punch.getType().name());
	        return punchDTO;
	    }).collect(Collectors.toList());
	    
	    responseDTO.setPunches(punchDTOs);
	    
	    return responseDTO;
	}
	
	public List<RequestDTO> getRequestsByUserId(Long userId) {
	    User currentUser = userService.findEntityById(userId)
	            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	    if (!"COORDENADOR".equals(currentUser.getTag().name()) && !"ADMINISTRADOR".equals(currentUser.getTag().name())) {
	        throw new RuntimeException("Acesso proibido");
	    }

	    List<Request> requests = findByOrganizationId(currentUser.getTeam().getOrganization().getId());

	    return requests.stream().map(request -> {
	        RequestDTO requestDTO = new RequestDTO();
	        requestDTO.setId(request.getId());
	        requestDTO.setJustification(request.getJustification());
	        requestDTO.setRequestType(request.getRequestType().toString());
	        requestDTO.setStatus(request.getStatus().toString());

	        User user = request.getUser();
	        if (user != null) {
	            UserDTO userDTO = new UserDTO();
	            userDTO.setId(user.getId());
	            userDTO.setName(user.getName());
	            requestDTO.setUser(userDTO);
	        }

	        if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
	            List<PunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(tempPunch -> {
	                PunchDTO punchDTO = new PunchDTO();
	                punchDTO.setStatus(tempPunch.getType().toString()); 
	                punchDTO.setHours(tempPunch.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
	                return punchDTO;
	            }).collect(Collectors.toList());

	            requestDTO.setPunches(punchDTOs); 
	        }

	        return requestDTO;
	    }).collect(Collectors.toList());
	}

	
	public Request approveRequest(Long requestId, ApprovalRequestDTO approvalRequestDTO) {
	    Request request = findById(requestId)
	            .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

	    request.setStatus(approvalRequestDTO.getStatus());

	    if (approvalRequestDTO.getStatus() == RequestStatusEnum.APROVADO) {
	        if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
	            for (TemporaryPunch tempPunch : request.getTemporaryPunches()) {
	                PunchLog punchLog = new PunchLog();
	                punchLog.setUser(request.getUser());
	                punchLog.setTimestamp(tempPunch.getTimestamp());
	                punchLog.setType(tempPunch.getType());
	                punchLog.setOrganization(request.getOrganization());

	                punchLogService.save(punchLog); 
	            }
	        }
	    }

	    return save(request); 
	}
	
	public Map<String, Long> countRequestsByStatus(Long teamId) {
	    Map<String, Long> counts = new HashMap<>();
	    counts.put("pendente", countByTeamIdAndStatus(teamId, RequestStatusEnum.PENDENTE));
	    counts.put("aprovado", countByTeamIdAndStatus(teamId, RequestStatusEnum.APROVADO));
	    counts.put("reprovado", countByTeamIdAndStatus(teamId, RequestStatusEnum.REPROVADO));
	    return counts;
	}
	
	public List<RequestDTO> getFilteredRequests(RequestFilterDTO filterDTO) {
	    List<Request> requests;

	    // Verifica se o teamId foi fornecido
	    if (filterDTO.getTeamId() != null) {
	        // Converte os tipos e status de String para Enum, se fornecidos
	        List<RequestTypeEnum> types = null;
	        if (filterDTO.getTypes() != null && !filterDTO.getTypes().isEmpty()) {
	            types = filterDTO.getTypes().stream()
	                    .map(RequestTypeEnum::valueOf)
	                    .collect(Collectors.toList());
	        }

	        List<RequestStatusEnum> statuses = null;
	        if (filterDTO.getStatuses() != null && !filterDTO.getStatuses().isEmpty()) {
	            statuses = filterDTO.getStatuses().stream()
	                    .map(RequestStatusEnum::valueOf)
	                    .collect(Collectors.toList());
	        }

	        // Se tipos e status foram fornecidos, usa o método com filtros adicionais
	        if (types != null && statuses != null) {
	            requests = requestRepository.findByUserTeamIdAndTypeInAndStatusIn(filterDTO.getTeamId(), types, statuses);
	        } else if (types != null) {
	            // Implementar método no repositório para filtrar por tipos
	            requests = requestRepository.findByUserTeamIdAndTypeIn(filterDTO.getTeamId(), types);
	        } else if (statuses != null) {
	            // Implementar método no repositório para filtrar por status
	            requests = requestRepository.findByUserTeamIdAndStatusIn(filterDTO.getTeamId(), statuses);
	        } else {
	            requests = requestRepository.findByUserTeamId(filterDTO.getTeamId());
	        }
	    } else {
	        // Se nenhum teamId foi fornecido, pode buscar todas as solicitações ou retornar vazio
	        requests = new ArrayList<>(); // Ou lance uma exceção, dependendo da lógica do seu aplicativo
	    }

	    // Mapeia as solicitações para DTOs
	    return requests.stream()
	            .map(this::mapToFilterDTO)
	            .collect(Collectors.toList());
	}



	public RequestDTO mapToFilterDTO(Request request) {
	    RequestDTO dto = new RequestDTO();
	    dto.setId(request.getId());
	    dto.setRequestType(request.getRequestType().name());
	    dto.setJustification(request.getJustification());
	    dto.setStatus(request.getStatus().name());

	    // Mapeando o usuário
	    User user = request.getUser();
	    if (user != null) {
	        UserDTO userDTO = new UserDTO(user.getId(), user.getName());
	        dto.setUser(userDTO);
	    }

	    // Mapeando os punches, se existirem
	    if (request.getTemporaryPunches() != null && !request.getTemporaryPunches().isEmpty()) {
	        List<PunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(tempPunch -> {
	            PunchDTO punchDTO = new PunchDTO();
	            punchDTO.setStatus(tempPunch.getType().name());
	            punchDTO.setHours(tempPunch.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
	            return punchDTO;
	        }).collect(Collectors.toList());

	        dto.setPunches(punchDTOs);
	    }

	    return dto;
	}





}

package com.wisetime.wisetime.service.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.certificate.CertificateDTO;
import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.punch.PunchDTO;
import com.wisetime.wisetime.DTO.punch.TemporaryPunchDTO;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.DTO.request.RequestFilterDTO;
import com.wisetime.wisetime.DTO.request.RequestResponseDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;
import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.request.RequestStatusEnum;
import com.wisetime.wisetime.models.request.RequestTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.request.RequestRepository;
import com.wisetime.wisetime.service.audit.AuditService;
import com.wisetime.wisetime.service.certificate.CertificateService;
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
    
    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private AuditService auditService;

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

            Request savedRequest = this.save(request);
            temporaryPunchService.saveAll(request.getTemporaryPunches());
            auditService.logAction("Adição de ponto", user, "O Usuário abriu uma solicitação de Adição de ponto");

            return savedRequest;

        } else if (requestDTO.getRequestType().equals("ATESTADO")) {
            CertificateDTO certDTO = requestDTO.getCertificate();
            Certificate certificate = new Certificate();
            certificate.setUser(user);
            certificate.setStartDate(LocalDate.parse(certDTO.getStartDate(), DateTimeFormatter.ISO_DATE));
            certificate.setEndDate(LocalDate.parse(certDTO.getEndDate(), DateTimeFormatter.ISO_DATE));
            certificate.setJustification(requestDTO.getJustification());
            certificate.setStatus(CertificateStatusEnum.PENDENTE);
            certificate.setRequest(request);

            if (certDTO.getImageBase64() != null && !certDTO.getImageBase64().isEmpty()) {
                String base64Data = certDTO.getImageBase64();

                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];
                }

                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                certificate.setImageData(imageBytes);
            }

            request.setCertificate(certificate);

            Request savedRequest = this.save(request);
            auditService.logAction("Adição de Atestado", user, "O Usuário abriu uma solicitação de Atestado");
            return savedRequest;

        } else {
            throw new RuntimeException("Tipo de solicitação não suportado");
        }
    }

    public RequestResponseDTO mapToDTO(Request request) {
        RequestResponseDTO responseDTO = new RequestResponseDTO();
        responseDTO.setId(request.getId());
        responseDTO.setRequestType(request.getRequestType().name());
        responseDTO.setJustification(request.getJustification());
        responseDTO.setStatus(request.getStatus().name());

        User user = request.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getName());
            responseDTO.setUser(userDTO);
        }

        if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
            List<TemporaryPunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(punch -> {
                TemporaryPunchDTO punchDTO = new TemporaryPunchDTO();
                punchDTO.setHours(punch.getTimestamp().toString());
                punchDTO.setStatus(punch.getType().name());
                return punchDTO;
            }).collect(Collectors.toList());

            responseDTO.setPunches(punchDTOs);
        } else if (request.getRequestType() == RequestTypeEnum.ATESTADO) {
            Certificate certificate = request.getCertificate();
            if (certificate != null) {
                CertificateDTO certificateDTO = new CertificateDTO();
                certificateDTO.setStartDate(certificate.getStartDate().toString());
                certificateDTO.setEndDate(certificate.getEndDate().toString());
                certificateDTO.setJustification(certificate.getJustification());
                certificateDTO.setStatus(certificate.getStatus().name());
                responseDTO.setCertificate(certificateDTO);
            }
        }

        return responseDTO;
    }

    public Request approveRequest(Long requestId, ApprovalRequestDTO approvalRequestDTO) {
    	User user = userService.findEntityById(approvalRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    	
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
                    auditService.logAction("Aprovou Solicitação", user, "O Coordenador aprovou uma solicitação de Adição de ponto do usuário: " + request.getUser().getName());
                }
            } else if (request.getRequestType() == RequestTypeEnum.ATESTADO) {
                Certificate certificate = request.getCertificate();
                certificate.setStatus(CertificateStatusEnum.APROVADO);
                certificateService.save(certificate);
                auditService.logAction("Aprovou Solicitação", user, "O Coordenador aprovou uma solicitação de Atestado do usuário: " + request.getUser().getName());
            }
        } else if (approvalRequestDTO.getStatus() == RequestStatusEnum.REPROVADO) {
            if (request.getRequestType() == RequestTypeEnum.ATESTADO) {
                Certificate certificate = request.getCertificate();
                certificate.setStatus(CertificateStatusEnum.REPROVADO);
                certificateService.save(certificate);
                auditService.logAction("Reprovou Solicitação", user, "O Coordenador reprovou uma solicitação de Atestado do usuário: " + request.getUser().getName());
            }
        }

        return save(request);
    }
    
    public List<RequestDTO> getRequestsByUserId(Long userId) {
        User user = userService.findEntityById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Request> requests = requestRepository.findByUserId(userId);

        return requests.stream().map(request -> {
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setId(request.getId());
            requestDTO.setJustification(request.getJustification());
            requestDTO.setRequestType(request.getRequestType().toString());
            requestDTO.setStatus(request.getStatus().toString());

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            requestDTO.setUser(userDTO);

            if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
                List<PunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(tempPunch -> {
                    PunchDTO punchDTO = new PunchDTO();
                    punchDTO.setStatus(tempPunch.getType().toString());
                    punchDTO.setHours(tempPunch.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
                    return punchDTO;
                }).collect(Collectors.toList());

                requestDTO.setPunches(punchDTOs);
            } else if (request.getRequestType() == RequestTypeEnum.ATESTADO) {
                Certificate certificate = request.getCertificate();
                if (certificate != null) {
                    CertificateDTO certificateDTO = new CertificateDTO();
                    certificateDTO.setStartDate(certificate.getStartDate().toString());
                    certificateDTO.setEndDate(certificate.getEndDate().toString());
                    certificateDTO.setJustification(certificate.getJustification());
                    certificateDTO.setStatus(certificate.getStatus().name());
                    requestDTO.setCertificate(certificateDTO);
                }
            }

            return requestDTO;
        }).collect(Collectors.toList());
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

	    if (filterDTO.getTeamId() != null) {
	        List<RequestTypeEnum> requestTypes = null;
	        if (filterDTO.getTypes() != null && !filterDTO.getTypes().isEmpty()) {
	            requestTypes = filterDTO.getTypes().stream()
	                    .map(RequestTypeEnum::valueOf)
	                    .collect(Collectors.toList());
	        }

	        List<RequestStatusEnum> statuses = null;
	        if (filterDTO.getStatuses() != null && !filterDTO.getStatuses().isEmpty()) {
	            statuses = filterDTO.getStatuses().stream()
	                    .map(RequestStatusEnum::valueOf)
	                    .collect(Collectors.toList());
	        }

	        if (requestTypes != null && statuses != null) {
	            requests = requestRepository.findByUserTeamIdAndRequestTypeInAndStatusIn(filterDTO.getTeamId(), requestTypes, statuses);
	        } else if (requestTypes != null) {
	            requests = requestRepository.findByUserTeamIdAndRequestTypeIn(filterDTO.getTeamId(), requestTypes);
	        } else if (statuses != null) {
	            requests = requestRepository.findByUserTeamIdAndStatusIn(filterDTO.getTeamId(), statuses);
	        } else {
	            requests = requestRepository.findByUserTeamId(filterDTO.getTeamId());
	        }
	    } else {
	        requests = new ArrayList<>(); 
	    }

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

        User user = request.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getName());
            dto.setUser(userDTO);
        }

        if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
        	if (request.getTemporaryPunches() != null && !request.getTemporaryPunches().isEmpty()) {
    	        List<PunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(tempPunch -> {
    	            PunchDTO punchDTO = new PunchDTO();
    	            punchDTO.setStatus(tempPunch.getType().name());
    	            punchDTO.setHours(tempPunch.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    	            return punchDTO;
    	        }).collect(Collectors.toList());

    	        dto.setPunches(punchDTOs);
    	    }
        } else if (request.getRequestType() == RequestTypeEnum.ATESTADO) {
            Certificate certificate = request.getCertificate();
            if (certificate != null) {
                CertificateDTO certificateDTO = new CertificateDTO();
                certificateDTO.setId(certificate.getId());
                certificateDTO.setStartDate(certificate.getStartDate().toString());
                certificateDTO.setEndDate(certificate.getEndDate().toString());
                certificateDTO.setJustification(certificate.getJustification());
                certificateDTO.setStatus(certificate.getStatus().name());
                dto.setCertificate(certificateDTO);
            }
        }

        return dto;
    }

}

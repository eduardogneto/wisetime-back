package com.wisetime.wisetime.controller.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.punch.PunchDTO;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.request.Request;
import com.wisetime.wisetime.request.RequestStatusEnum;
import com.wisetime.wisetime.request.RequestTypeEnum;
import com.wisetime.wisetime.service.punch.PunchLogService;
import com.wisetime.wisetime.service.punch.TemporaryPunchService;
import com.wisetime.wisetime.service.request.RequestService;
import com.wisetime.wisetime.service.user.UserService;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PunchLogService punchLogService;

    @Autowired
    private TemporaryPunchService temporaryPunchService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestBody RequestDTO requestDTO) {
        // Buscar o usuário pelo ID fornecido no DTO
        User user = userService.findById(requestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Request request = new Request();
        request.setUser(user);  // Relaciona o usuário diretamente com a solicitação
        request.setOrganization(user.getRole().getOrganization());
        request.setRequestType(RequestTypeEnum.valueOf(requestDTO.getRequestType()));
        request.setJustification(requestDTO.getJustification());
        request.setStatus(RequestStatusEnum.PENDENTE);

        // Se for uma requisição de adição de ponto, cria TemporaryPunches
        if (requestDTO.getRequestType().equals("ADICAO_DE_PONTO")) {
            List<TemporaryPunch> temporaryPunches = requestDTO.getPunches().stream().map(punchDTO -> {
                TemporaryPunch tempPunch = new TemporaryPunch();
                tempPunch.setTimestamp(LocalDateTime.parse(punchDTO.getHours(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                tempPunch.setType(punchDTO.getStatus().equals("Entrada") ? PunchTypeEnum.ENTRY : PunchTypeEnum.EXIT);
                tempPunch.setUser(user);  // Relaciona com o usuário
                tempPunch.setRequest(request); // Relaciona com a requisição
                return tempPunch;
            }).collect(Collectors.toList());

            request.setTemporaryPunches(temporaryPunches);
        }

        Request savedRequest = requestService.save(request); // Salva a requisição
        temporaryPunchService.saveAll(request.getTemporaryPunches()); // Salva os punches temporários
        return ResponseEntity.ok(savedRequest);
    }


    // Listar solicitações por coordenador
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<RequestDTO>> getRequests(@PathVariable Long userId) {
        User currentUser = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!"COORDENADOR".equals(currentUser.getTag().name()) && !"ADMINISTRADOR".equals(currentUser.getTag().name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<Request> requests = requestService.findByOrganizationId(
                currentUser.getRole().getOrganization().getId());

        List<RequestDTO> requestDTOs = requests.stream().map(request -> {
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setId(request.getId());
            requestDTO.setJustification(request.getJustification());
            requestDTO.setRequestType(request.getRequestType().toString());
            requestDTO.setStatus(request.getStatus().toString());
            requestDTO.setUser(request.getUser());  // Agora enviando o objeto `User` diretamente

            // Se for ADICAO_DE_PONTO, trazer os punches temporários
            if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
                List<PunchDTO> punchDTOs = request.getTemporaryPunches().stream().map(tempPunch -> {
                    PunchDTO punchDTO = new PunchDTO();
                    punchDTO.setStatus(tempPunch.getType().toString()); // Entrada ou Saída
                    punchDTO.setHours(tempPunch.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
                    return punchDTO;
                }).collect(Collectors.toList());

                requestDTO.setPunches(punchDTOs); // Adiciona os punches ao DTO
            }

            return requestDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    // Aprovar solicitação
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<Request> approveRequest(@PathVariable Long requestId, @RequestBody ApprovalRequestDTO approvalRequestDTO) {
        Request request = requestService.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        request.setStatus(approvalRequestDTO.getStatus());

        if (approvalRequestDTO.getStatus() == RequestStatusEnum.APROVADO) {
            if (request.getRequestType() == RequestTypeEnum.ADICAO_DE_PONTO) {
                // Mover punches temporários para PunchLog
                for (TemporaryPunch tempPunch : request.getTemporaryPunches()) {
                    PunchLog punchLog = new PunchLog();
                    punchLog.setUser(request.getUser());
                    punchLog.setTimestamp(tempPunch.getTimestamp());
                    punchLog.setType(tempPunch.getType());
                    punchLog.setOrganization(request.getOrganization());

                    punchLogService.save(punchLog); // Salvar punch aprovado
                }
            }
        }

        Request updatedRequest = requestService.save(request);
        return ResponseEntity.ok(updatedRequest);
    }
    
    @GetMapping("/countByStatus/{organizationId}")
    public ResponseEntity<Map<String, Long>> countRequestsByStatus(@PathVariable Long organizationId) {
        Map<String, Long> counts = new HashMap<>();
        counts.put("pendente", requestService.countByOrganizationIdAndStatus(organizationId, RequestStatusEnum.PENDENTE));
        counts.put("aprovado", requestService.countByOrganizationIdAndStatus(organizationId, RequestStatusEnum.APROVADO));
        counts.put("reprovado", requestService.countByOrganizationIdAndStatus(organizationId, RequestStatusEnum.REPROVADO));
        return ResponseEntity.ok(counts);
    }
}


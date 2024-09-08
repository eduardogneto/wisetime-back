package com.wisetime.wisetime.controller.punch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.punch.PunchEditDTO;
import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchRequest;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.punch.PunchLogService;
import com.wisetime.wisetime.service.punch.PunchRequestService;
import com.wisetime.wisetime.service.user.UserService;

@RestController
@RequestMapping("/api/punch")
public class PunchController {

    @Autowired
    private UserService userService;

    @Autowired
    private PunchLogService punchLogService;

    @Autowired
    private PunchRequestService punchRequestService;

    @PostMapping("/log")
    public ResponseEntity<PunchLog> logPunch(@RequestBody PunchLogDTO punchLogDTO) {
        User user = userService.findById(punchLogDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + punchLogDTO.getUserId()));

        // Buscar a última batida de ponto do usuário
        Optional<PunchLog> lastPunchOpt = punchLogService.getLastPunchLog(user.getId());

        PunchLog punchLog = new PunchLog();
        punchLog.setUser(user);
        punchLog.setTimestamp(punchLogDTO.getTimestamp());
        punchLog.setLocation(punchLogDTO.getLocation());
        punchLog.setOrganization(user.getRole().getOrganization());

        if (lastPunchOpt.isPresent()) {
            PunchLog lastPunch = lastPunchOpt.get();
            PunchTypeEnum lastPunchType = lastPunch.getType();

            if (lastPunchType == PunchTypeEnum.ENTRY) {
                punchLog.setType(PunchTypeEnum.EXIT);  // A última foi entrada, então agora é saída
            } else {
                punchLog.setType(PunchTypeEnum.ENTRY);  // A última foi saída, então agora é entrada
            }
        } else {
            punchLog.setType(PunchTypeEnum.ENTRY);  // Se não há registros anteriores, a primeira batida será uma entrada
        }

        PunchLog savedPunchLog = punchLogService.save(punchLog);
        return ResponseEntity.ok(savedPunchLog);
    }

    @PostMapping("/edit")
    public ResponseEntity<PunchLog> editPunchLog(@RequestBody PunchEditDTO punchEditDTO) {
        User user = userService.findById(punchEditDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + punchEditDTO.getUserId()));

        PunchLog punchLog = punchLogService.findById(punchEditDTO.getPunchLogId());
        PunchTypeEnum previousType = punchLog.getType(); // Guardar o tipo anterior

        punchLog.setTimestamp(punchEditDTO.getNewTimestamp());

        if (punchEditDTO.getType() != null) {
            punchLog.setType(punchEditDTO.getType());
        }

        if (punchEditDTO.getLocation() != null) {
            punchLog.setLocation(punchEditDTO.getLocation());
        }

        PunchLog updatedPunchLog = punchLogService.save(punchLog);

        if (previousType != punchEditDTO.getType()) {
            punchLogService.adjustSubsequentPunchLogs(user.getId(), punchLog.getTimestamp(), punchEditDTO.getType());
        }

        return ResponseEntity.ok(updatedPunchLog);
    }


    // Endpoint para buscar o histórico de pontos do usuário em um determinado dia
    @GetMapping("/history/{userId}/{date}")
    public ResponseEntity<List<PunchLog>> getPunchHistoryForDay(@PathVariable Long userId, @PathVariable LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<PunchLog> punchLogs = punchLogService.getPunchLogsByUserAndDateRange(userId, startOfDay, endOfDay);

        if (punchLogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(punchLogs);
    }

    // Endpoint para aprovar ou reprovar uma solicitação de edição de ponto
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<PunchRequest> approveOrRejectRequest(@PathVariable Long requestId, @RequestBody ApprovalRequestDTO approvalRequestDTO) {
        PunchRequest request = punchRequestService.findById(requestId);
        request.setStatus(approvalRequestDTO.getStatus());

        PunchRequest updatedRequest = punchRequestService.save(request);

        return ResponseEntity.ok(updatedRequest);
    }
    
    @GetMapping("/history/summary/{userId}")
    public ResponseEntity<List<PunchSummaryDTO>> getPunchHistorySummary(@PathVariable Long userId) {
        List<PunchLog> punchLogs = punchLogService.getPunchLogsByUser(userId);

        if (punchLogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Map para agrupar por data
        Map<String, PunchSummaryDTO> punchSummaryMap = new HashMap<>();

        for (PunchLog punch : punchLogs) {
            String date = punch.getTimestamp().toLocalDate().toString();
            PunchSummaryDTO summary = punchSummaryMap.getOrDefault(date, new PunchSummaryDTO());
            summary.setDate(date);

            if (punch.getType() == PunchTypeEnum.ENTRY) {
                summary.setEntryCount(summary.getEntryCount() + 1);
            } else {
                summary.setExitCount(summary.getExitCount() + 1);
            }

            // Definir se está completo ou incompleto
            summary.setStatus(summary.getEntryCount() == summary.getExitCount() ? "Completo" : "Incompleto");

            punchSummaryMap.put(date, summary);
        }

        List<PunchSummaryDTO> punchSummaries = punchSummaryMap.values().stream().collect(Collectors.toList());
        return ResponseEntity.ok(punchSummaries);
    }
}

package com.wisetime.wisetime.controller.punch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchLogResponseDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.punch.PunchLogService;

@RestController
@RequestMapping("/api/punch")
public class PunchController {

    @Autowired
    private PunchLogService punchLogService;

    @PostMapping("/log")
    public ResponseEntity<PunchLogResponseDTO> logPunch(@RequestBody PunchLogDTO punchLogDTO) {
        PunchLog savedPunchLog = punchLogService.logPunch(punchLogDTO);
        PunchLogResponseDTO responseDTO = convertToDTO(savedPunchLog);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/history/{userId}/{date}")
    public ResponseEntity<List<PunchLogResponseDTO>> getPunchHistoryForDay(
            @PathVariable Long userId, 
            @PathVariable LocalDate date) {
        
        List<PunchLog> punchLogs = punchLogService.getPunchHistoryForDay(userId, date);
//        if (punchLogs.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
        
        List<PunchLogResponseDTO> response = punchLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/summary/{userId}")
    public ResponseEntity<List<PunchSummaryDTO>> getPunchHistorySummary(@PathVariable Long userId) {
        List<PunchSummaryDTO> punchSummaries = punchLogService.getPunchHistorySummary(userId);
        if (punchSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(punchSummaries);
    }
    
    
    private PunchLogResponseDTO convertToDTO(PunchLog punchLog) {
        User user = punchLog.getUser();
        UserDTO userDTO = new UserDTO(user.getId(), user.getName());
        return new PunchLogResponseDTO(
                punchLog.getId(),
                userDTO,
                punchLog.getTimestamp(),
                punchLog.getType(),
                punchLog.getLocation(),
                punchLog.getOrganization().getId()
        );
    }
}


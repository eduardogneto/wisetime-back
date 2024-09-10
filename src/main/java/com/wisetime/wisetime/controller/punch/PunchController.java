package com.wisetime.wisetime.controller.punch;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.service.punch.PunchLogService;

@RestController
@RequestMapping("/api/punch")
public class PunchController {

    @Autowired
    private PunchLogService punchLogService;

    @PostMapping("/log")
    public ResponseEntity<PunchLog> logPunch(@RequestBody PunchLogDTO punchLogDTO) {
        PunchLog savedPunchLog = punchLogService.logPunch(punchLogDTO);
        return ResponseEntity.ok(savedPunchLog);
    }

    @GetMapping("/history/{userId}/{date}")
    public ResponseEntity<List<PunchLog>> getPunchHistoryForDay(@PathVariable Long userId, @PathVariable LocalDate date) {
        List<PunchLog> punchLogs = punchLogService.getPunchHistoryForDay(userId, date);
        if (punchLogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(punchLogs);
    }

    @GetMapping("/history/summary/{userId}")
    public ResponseEntity<List<PunchSummaryDTO>> getPunchHistorySummary(@PathVariable Long userId) {
        List<PunchSummaryDTO> punchSummaries = punchLogService.getPunchHistorySummary(userId);
        if (punchSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(punchSummaries);
    }
}


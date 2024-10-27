package com.wisetime.wisetime.controller.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.audit.AuditLogDTO;
import com.wisetime.wisetime.service.audit.AuditService;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/logs/{teamId}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs(@PathVariable Long teamId) {
        try {
            List<AuditLogDTO> logs = auditService.getAuditLogs(teamId);
            
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

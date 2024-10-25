package com.wisetime.wisetime.service.audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.audit.AuditLogDTO;
import com.wisetime.wisetime.models.audit.AuditLog;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.audit.AuditLogRepository;

@Service
public class AuditService {

	private final AuditLogRepository auditLogRepository;

	@Autowired
	public AuditService(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	public List<AuditLogDTO> getAuditLogs(Long teamId) {
	    List<AuditLog> audits = auditLogRepository.findByUserTeamId(teamId);
	    
	    return audits.stream().map(audit -> {
	        AuditLogDTO requestDTO = new AuditLogDTO();
	        requestDTO.setId(audit.getId());
	        requestDTO.setAction(audit.getAction());
	        requestDTO.setName(audit.getUser().getName());
	        requestDTO.setDetails(audit.getDetails());
	        requestDTO.setDate(audit.getDate());
	        return requestDTO; 
	    }).collect(Collectors.toList()); 
	}
	
	public void logAction(String action, User user, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLog.setDetails(details);
        auditLog.setDate(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

}

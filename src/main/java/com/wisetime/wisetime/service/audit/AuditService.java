package com.wisetime.wisetime.service.audit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

	public List<AuditLog> getAuditLogs(String action, String user, String startDate, String endDate) {
	    Specification<AuditLog> spec = Specification.where(null);

	    if (action != null && !action.isEmpty()) {
	        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("acao"), action));
	    }

	    if (user != null && !user.isEmpty()) {
	        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("usuario").get("name"),
	                "%" + user + "%"));
	    }

	    return auditLogRepository.findAll(spec);
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

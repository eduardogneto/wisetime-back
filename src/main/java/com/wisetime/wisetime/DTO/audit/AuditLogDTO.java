package com.wisetime.wisetime.DTO.audit;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuditLogDTO {
	private Long id;
    private String action;
    private String name;
    private String details;
    private LocalDateTime date; 
}

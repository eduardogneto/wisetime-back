package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.request.RequestStatusEnum;

import lombok.Data;

@Data
public class ApprovalRequestDTO {
	private Long userId;
	private RequestStatusEnum status; 
    private String justification;
    private LocalDateTime newTimestamp;  
}

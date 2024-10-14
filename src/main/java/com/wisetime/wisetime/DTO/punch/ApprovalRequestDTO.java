package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.request.RequestStatusEnum;


public class ApprovalRequestDTO {

	private RequestStatusEnum status; 
    private String justification;
    private LocalDateTime newTimestamp;  

    public RequestStatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getNewTimestamp() {
		return newTimestamp;
	}

	public void setNewTimestamp(LocalDateTime newTimestamp) {
		this.newTimestamp = newTimestamp;
	}

	public void setStatus(RequestStatusEnum status) {
        this.status = status;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}

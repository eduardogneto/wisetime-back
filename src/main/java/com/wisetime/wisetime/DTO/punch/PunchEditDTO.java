package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.punch.PunchTypeEnum;

public class PunchEditDTO {
    private Long userId;
    private Long punchLogId;
    private LocalDateTime newTimestamp;
    private PunchTypeEnum type; 
    private String location;

    public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPunchLogId() {
        return punchLogId;
    }

    public void setPunchLogId(Long punchLogId) {
        this.punchLogId = punchLogId;
    }

    public LocalDateTime getNewTimestamp() {
        return newTimestamp;
    }

    public void setNewTimestamp(LocalDateTime newTimestamp) {
        this.newTimestamp = newTimestamp;
    }

    public PunchTypeEnum getType() {
        return type;
    }

    public void setType(PunchTypeEnum type) {
        this.type = type;
    }
}

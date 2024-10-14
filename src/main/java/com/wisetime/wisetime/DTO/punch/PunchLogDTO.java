package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.punch.PunchTypeEnum;

public class PunchLogDTO {

    private Long userId;
    private LocalDateTime timestamp;
    private PunchTypeEnum type;
    private String location;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public PunchTypeEnum getType() {
        return type;
    }

    public void setType(PunchTypeEnum type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

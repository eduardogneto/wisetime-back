package com.wisetime.wisetime.DTO.punch;

import com.wisetime.wisetime.models.punch.ApprovalStatusEnum;

public class ApprovalRequestDTO {

    private ApprovalStatusEnum status;

    // Getters e Setters
    public ApprovalStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatusEnum status) {
        this.status = status;
    }
}

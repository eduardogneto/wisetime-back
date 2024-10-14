package com.wisetime.wisetime.DTO.dueDateBank;

import com.wisetime.wisetime.models.dueDateBank.TagDueDateBankEnum;

import java.time.LocalDate;

public class DueDateBankDTO {
    private Long id; 
    private LocalDate startDate;
    private LocalDate endDate;
    private TagDueDateBankEnum tag;
    private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TagDueDateBankEnum getTag() {
        return tag;
    }

    public void setTag(TagDueDateBankEnum tag) {
        this.tag = tag;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}

package com.wisetime.wisetime.models.dueDateBank;

import com.wisetime.wisetime.models.organization.Organization;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "due_date_bank")
public class DueDateBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Transient
    private TagDueDateBankEnum tag;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public DueDateBank() {
    }

    public DueDateBank(LocalDate startDate, LocalDate endDate, Organization organization) {
    	this.endDate = endDate;
        this.startDate = startDate;
        this.organization = organization;
    }

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
    	LocalDate today = LocalDate.now();
        if (today.isBefore(this.startDate)) {
            return TagDueDateBankEnum.PROXIMO;
        } else if ((today.isEqual(this.startDate) || today.isAfter(this.startDate)) && today.isBefore(this.endDate)) {
            return TagDueDateBankEnum.ANDAMENTO;
        } else if (today.isEqual(this.endDate) || today.isAfter(this.endDate)) {
            return TagDueDateBankEnum.COMPLETO;
        } else {
            return null; 
        }
    }

    public void setTag(TagDueDateBankEnum tag) {
        this.tag = tag;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}

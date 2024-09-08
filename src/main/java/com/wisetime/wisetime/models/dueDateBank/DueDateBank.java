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

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private TagDueDateBankEnum tag;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public DueDateBank() {
    }

    // Construtor correto com todos os parâmetros, incluindo Organization
    public DueDateBank(LocalDate startDate, LocalDate endDate, TagDueDateBankEnum tag, Organization organization) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tag = tag;
        this.organization = organization;
    }

    // Getters e Setters
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}

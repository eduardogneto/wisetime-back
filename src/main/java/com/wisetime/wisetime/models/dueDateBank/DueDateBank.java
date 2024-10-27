package com.wisetime.wisetime.models.dueDateBank;
import java.time.LocalDate;

import com.wisetime.wisetime.models.organization.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "due_date_bank")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public DueDateBank(LocalDate startDate, LocalDate endDate, Organization organization) {
    	this.endDate = endDate;
        this.startDate = startDate;
        this.organization = organization;
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
}

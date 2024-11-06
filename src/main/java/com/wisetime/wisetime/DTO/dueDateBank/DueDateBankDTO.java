package com.wisetime.wisetime.DTO.dueDateBank;

import java.time.LocalDate;

import com.wisetime.wisetime.models.dueDateBank.TagDueDateBankEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DueDateBankDTO {
    private Long id; 
    private LocalDate startDate;
    private LocalDate endDate;
    private TagDueDateBankEnum tag;
    private Long organizationId;
}

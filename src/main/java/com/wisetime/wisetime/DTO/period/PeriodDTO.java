package com.wisetime.wisetime.DTO.period;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodDTO {
	private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

   
}
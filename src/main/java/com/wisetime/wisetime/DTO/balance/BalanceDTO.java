package com.wisetime.wisetime.DTO.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
	
    private String currentPeriodBalance;
    private String previousPeriodBalance;
    private String totalBalance;
}

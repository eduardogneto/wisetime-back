package com.wisetime.wisetime.DTO.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {

    private String userName;
    private Long totalBalanceInSeconds;
}

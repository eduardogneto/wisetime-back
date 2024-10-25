package com.wisetime.wisetime.DTO.balance;

import lombok.Data;

@Data
public class UserBalanceDTO {

    private String userName;
    private Long totalBalanceInSeconds;

    public UserBalanceDTO(String userName, Long totalBalanceInSeconds) {
        this.userName = userName;
        this.totalBalanceInSeconds = totalBalanceInSeconds;
    }

}

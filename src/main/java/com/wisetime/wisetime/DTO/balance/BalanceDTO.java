package com.wisetime.wisetime.DTO.balance;

public class BalanceDTO {
    private String currentPeriodBalance;
    private String previousPeriodBalance;
    private String totalBalance;

    public String getCurrentPeriodBalance() {
        return currentPeriodBalance;
    }

    public void setCurrentPeriodBalance(String currentPeriodBalance) {
        this.currentPeriodBalance = currentPeriodBalance;
    }

    public String getPreviousPeriodBalance() {
        return previousPeriodBalance;
    }

    public void setPreviousPeriodBalance(String previousPeriodBalance) {
        this.previousPeriodBalance = previousPeriodBalance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}

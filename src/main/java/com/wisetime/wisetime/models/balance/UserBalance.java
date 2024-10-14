package com.wisetime.wisetime.models.balance;

import com.wisetime.wisetime.models.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_balances")
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long currentPeriodBalanceInSeconds;
    private Long previousPeriodBalanceInSeconds;
    private Long totalBalanceInSeconds;

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Long getCurrentPeriodBalanceInSeconds() {
        return currentPeriodBalanceInSeconds;
    }

    public void setCurrentPeriodBalanceInSeconds(Long currentPeriodBalanceInSeconds) {
        this.currentPeriodBalanceInSeconds = currentPeriodBalanceInSeconds;
    }

    public Long getPreviousPeriodBalanceInSeconds() {
        return previousPeriodBalanceInSeconds;
    }

    public void setPreviousPeriodBalanceInSeconds(Long previousPeriodBalanceInSeconds) {
        this.previousPeriodBalanceInSeconds = previousPeriodBalanceInSeconds;
    }

    public Long getTotalBalanceInSeconds() {
        return totalBalanceInSeconds;
    }

    public void setTotalBalanceInSeconds(Long totalBalanceInSeconds) {
        this.totalBalanceInSeconds = totalBalanceInSeconds;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

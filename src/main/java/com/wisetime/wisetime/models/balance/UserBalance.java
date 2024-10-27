package com.wisetime.wisetime.models.balance;

import java.time.LocalDate;

import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

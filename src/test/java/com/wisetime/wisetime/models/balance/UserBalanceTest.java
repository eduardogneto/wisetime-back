package com.wisetime.wisetime.models.balance;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.wisetime.wisetime.models.user.User;

import org.junit.jupiter.api.Test;

public class UserBalanceTest {

    @Test
    public void testNoArgsConstructor() {
        UserBalance userBalance = new UserBalance();
        assertNotNull(userBalance);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User();
        user.setId(1L);
        LocalDate date = LocalDate.now();

        UserBalance userBalance = new UserBalance(
                1L,
                date,
                user,
                3600L,
                1800L,
                5400L
        );

        assertEquals(1L, userBalance.getId());
        assertEquals(date, userBalance.getDate());
        assertEquals(user, userBalance.getUser());
        assertEquals(3600L, userBalance.getCurrentPeriodBalanceInSeconds());
        assertEquals(1800L, userBalance.getPreviousPeriodBalanceInSeconds());
        assertEquals(5400L, userBalance.getTotalBalanceInSeconds());
    }

    @Test
    public void testBuilder() {
        User user = new User();
        user.setId(2L);
        LocalDate date = LocalDate.of(2023, 1, 1);

        UserBalance userBalance = UserBalance.builder()
                .id(2L)
                .date(date)
                .user(user)
                .currentPeriodBalanceInSeconds(7200L)
                .previousPeriodBalanceInSeconds(3600L)
                .totalBalanceInSeconds(10800L)
                .build();

        assertEquals(2L, userBalance.getId());
        assertEquals(date, userBalance.getDate());
        assertEquals(user, userBalance.getUser());
        assertEquals(7200L, userBalance.getCurrentPeriodBalanceInSeconds());
        assertEquals(3600L, userBalance.getPreviousPeriodBalanceInSeconds());
        assertEquals(10800L, userBalance.getTotalBalanceInSeconds());
    }

    @Test
    public void testSettersAndGetters() {
        UserBalance userBalance = new UserBalance();
        User user = new User();
        user.setId(3L);
        LocalDate date = LocalDate.of(2023, 6, 15);

        userBalance.setId(3L);
        userBalance.setDate(date);
        userBalance.setUser(user);
        userBalance.setCurrentPeriodBalanceInSeconds(3000L);
        userBalance.setPreviousPeriodBalanceInSeconds(2000L);
        userBalance.setTotalBalanceInSeconds(5000L);

        assertEquals(3L, userBalance.getId());
        assertEquals(date, userBalance.getDate());
        assertEquals(user, userBalance.getUser());
        assertEquals(3000L, userBalance.getCurrentPeriodBalanceInSeconds());
        assertEquals(2000L, userBalance.getPreviousPeriodBalanceInSeconds());
        assertEquals(5000L, userBalance.getTotalBalanceInSeconds());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(4L);
        User user2 = new User();
        user2.setId(5L);
        LocalDate date = LocalDate.of(2023, 7, 1);

        UserBalance balance1 = new UserBalance(4L, date, user1, 1000L, 2000L, 3000L);
        UserBalance balance2 = new UserBalance(4L, date, user1, 1000L, 2000L, 3000L);
        UserBalance balance3 = new UserBalance(5L, date, user2, 1500L, 2500L, 4000L);

        assertEquals(balance1, balance2);
        assertNotEquals(balance1, balance3);

        assertEquals(balance1.hashCode(), balance2.hashCode());
        assertNotEquals(balance1.hashCode(), balance3.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(6L);
        user.setName("Test User");

        String expectedString = "UserBalance(id=6, date=2023-08-20, user=User(id=6, name=Test User), currentPeriodBalanceInSeconds=4000, previousPeriodBalanceInSeconds=1000, totalBalanceInSeconds=5000)";
        assertEquals(expectedString, expectedString.toString());
    }
}

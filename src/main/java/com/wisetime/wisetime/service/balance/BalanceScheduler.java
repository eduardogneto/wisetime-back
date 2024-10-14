package com.wisetime.wisetime.service.balance;

import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceScheduler {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 1 * * ?") 
    public void calculateDailyBalances() {
        List<User> users = userService.findAllUsers();

        for (User user : users) {
            try {
                balanceService.calculateAndSaveUserBalance(user);
            } catch (Exception e) {
            }
        }
    }
}

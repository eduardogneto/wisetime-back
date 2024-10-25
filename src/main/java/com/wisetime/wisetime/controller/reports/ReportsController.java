package com.wisetime.wisetime.controller.reports;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.balance.UserBalanceDTO;
import com.wisetime.wisetime.service.reports.ReportsService;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/getPositiveHours/{teamId}")
    public ResponseEntity<Long> getPositiveHours(@PathVariable Long teamId) {
        long count = reportsService.countUsersWithPositiveBalance(teamId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/getNegativeHours/{teamId}")
    public ResponseEntity<Long> getNegativeHours(@PathVariable Long teamId) {
        long count = reportsService.countUsersWithNegativeBalance(teamId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/getAllHours/{teamId}")
    public ResponseEntity<Long> getAllHours(@PathVariable Long teamId) {
        long count = reportsService.getAllBalance(teamId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/getUserBalances/{teamId}")
    public ResponseEntity<List<UserBalanceDTO>> getUserBalances(@PathVariable Long teamId) {
        List<UserBalanceDTO> userBalances = reportsService.getUserBalancesByTeamId(teamId);
        return ResponseEntity.ok(userBalances);
    }
}

package com.wisetime.wisetime.service.reports;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.balance.UserBalanceDTO;
import com.wisetime.wisetime.repository.balance.UserBalanceRepository;

@Service
public class ReportsService {

    private final UserBalanceRepository userBalanceRepository;

    @Autowired
    public ReportsService(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    public long countUsersWithPositiveBalance(Long teamId) {
        return userBalanceRepository.countUsersWithPositiveBalanceByTeamId(teamId);
    }

	public long countUsersWithNegativeBalance(Long teamId) {
		return userBalanceRepository.countUsersWithNegativeBalanceByTeamId(teamId);
	}
	
	public long getAllBalance(Long teamId) {
		return userBalanceRepository.sumTotalBalanceByTeamId(teamId);
	}
	

    public List<UserBalanceDTO> getUserBalancesByTeamId(Long teamId) {
        return userBalanceRepository.findUserBalancesByTeamId(teamId);
    }
}

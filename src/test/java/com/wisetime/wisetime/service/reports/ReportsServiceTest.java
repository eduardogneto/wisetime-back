package com.wisetime.wisetime.service.reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.balance.UserBalanceDTO;
import com.wisetime.wisetime.repository.balance.UserBalanceRepository;

@ExtendWith(MockitoExtension.class)
public class ReportsServiceTest {

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @InjectMocks
    private ReportsService reportsService;

    private Long teamId;
    private long positiveCount;
    private long negativeCount;
    private long totalBalance;
    private List<UserBalanceDTO> userBalances;

    @BeforeEach
    public void setUp() {
        teamId = 1L;
        positiveCount = 5L;
        negativeCount = 2L;
        totalBalance = 1000L;
        userBalances = Arrays.asList(
            new UserBalanceDTO("User1", 200L),
            new UserBalanceDTO("User2", 300L)
        );
    }

    @Test
    public void testCountUsersWithPositiveBalance() {
        when(userBalanceRepository.countUsersWithPositiveBalanceByTeamId(teamId)).thenReturn(positiveCount);
        long result = reportsService.countUsersWithPositiveBalance(teamId);
        assertEquals(positiveCount, result);
        verify(userBalanceRepository).countUsersWithPositiveBalanceByTeamId(eq(teamId));
    }

    @Test
    public void testCountUsersWithNegativeBalance() {
        when(userBalanceRepository.countUsersWithNegativeBalanceByTeamId(teamId)).thenReturn(negativeCount);
        long result = reportsService.countUsersWithNegativeBalance(teamId);
        assertEquals(negativeCount, result);
        verify(userBalanceRepository).countUsersWithNegativeBalanceByTeamId(eq(teamId));
    }

    @Test
    public void testGetAllBalance() {
        when(userBalanceRepository.sumTotalBalanceByTeamId(teamId)).thenReturn(totalBalance);
        long result = reportsService.getAllBalance(teamId);
        assertEquals(totalBalance, result);
        verify(userBalanceRepository).sumTotalBalanceByTeamId(eq(teamId));
    }

    @Test
    public void testGetUserBalancesByTeamId() {
        when(userBalanceRepository.findUserBalancesByTeamId(teamId)).thenReturn(userBalances);
        List<UserBalanceDTO> result = reportsService.getUserBalancesByTeamId(teamId);
        assertEquals(userBalances.size(), result.size());
        assertEquals(userBalances, result);
        verify(userBalanceRepository).findUserBalancesByTeamId(eq(teamId));
    }
}

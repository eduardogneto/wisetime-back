package com.wisetime.wisetime.controller.reports;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetime.wisetime.DTO.balance.UserBalanceDTO;
import com.wisetime.wisetime.service.reports.ReportsService;

@ExtendWith(MockitoExtension.class)
public class ReportsControllerTest {

	@Mock
	private ReportsService reportsService;

	@InjectMocks
	private ReportsController reportsController;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testGetPositiveHours_Success() throws Exception {
		Long teamId = 1L;
		long positiveCount = 5L;

		when(reportsService.countUsersWithPositiveBalance(teamId)).thenReturn(positiveCount);

		mockMvc.perform(get("/api/reports/getPositiveHours/{teamId}", teamId)).andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(positiveCount)));

		verify(reportsService, times(1)).countUsersWithPositiveBalance(teamId);
	}

	@Test
	public void testGetNegativeHours_Success() throws Exception {
		Long teamId = 1L;
		long negativeCount = 3L;

		when(reportsService.countUsersWithNegativeBalance(teamId)).thenReturn(negativeCount);

		mockMvc.perform(get("/api/reports/getNegativeHours/{teamId}", teamId)).andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(negativeCount)));

		verify(reportsService, times(1)).countUsersWithNegativeBalance(teamId);
	}

	@Test
	public void testGetAllHours_Success() throws Exception {
		Long teamId = 1L;
		long totalBalance = 20L;

		when(reportsService.getAllBalance(teamId)).thenReturn(totalBalance);

		mockMvc.perform(get("/api/reports/getAllHours/{teamId}", teamId)).andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(totalBalance)));

		verify(reportsService, times(1)).getAllBalance(teamId);
	}

	@Test
	public void testGetUserBalances_Success() throws Exception {
		Long teamId = 1L;
		List<UserBalanceDTO> userBalances = List.of(new UserBalanceDTO("User1", 1000L),
				new UserBalanceDTO("User2", -500L));

		when(reportsService.getUserBalancesByTeamId(teamId)).thenReturn(userBalances);

		mockMvc.perform(get("/api/reports/getUserBalances/{teamId}", teamId)).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(userBalances)));

		verify(reportsService, times(1)).getUserBalancesByTeamId(teamId);
	}

	@Test
	public void testGetUserBalances_EmptyList() throws Exception {
		Long teamId = 1L;

		when(reportsService.getUserBalancesByTeamId(teamId)).thenReturn(emptyList());

		mockMvc.perform(get("/api/reports/getUserBalances/{teamId}", teamId)).andExpect(status().isOk())
				.andExpect(content().json("[]"));

		verify(reportsService, times(1)).getUserBalancesByTeamId(teamId);
	}

}

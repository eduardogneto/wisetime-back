package com.wisetime.wisetime.controller.punch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.punch.PunchLogService;

@ExtendWith(MockitoExtension.class)
public class PunchControllerTest {

    @Mock
    private PunchLogService punchLogService;

    @InjectMocks
    private PunchController punchController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(punchController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetPunchHistoryForDay_Success() throws Exception {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        PunchLog punchLog = new PunchLog();
        punchLog.setId(1L);
        punchLog.setTimestamp(LocalDateTime.now());
        punchLog.setType(PunchTypeEnum.ENTRY);
        punchLog.setLocation("Location");

        User user = new User();
        user.setId(userId);
        punchLog.setUser(user);

        Organization organization = new Organization();
        organization.setId(1L);
        punchLog.setOrganization(organization);


        when(punchLogService.getPunchHistoryForDay(eq(userId), eq(date))).thenReturn(List.of(punchLog));

        mockMvc.perform(get("/api/punch/history/{userId}/{date}", userId, date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(punchLog.getId()))
                .andExpect(jsonPath("$[0].user.id").value(userId))
                .andExpect(jsonPath("$[0].location").value("Location"));

        verify(punchLogService, times(1)).getPunchHistoryForDay(eq(userId), eq(date));
    }

    @Test
    public void testGetPunchHistoryForDay_NoContent() throws Exception {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        when(punchLogService.getPunchHistoryForDay(eq(userId), eq(date))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/punch/history/{userId}/{date}", userId, date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(punchLogService, times(1)).getPunchHistoryForDay(eq(userId), eq(date));
    }

    @Test
    public void testGetPunchHistorySummary_Success() throws Exception {
        Long userId = 1L;

        PunchSummaryDTO summaryDTO = new PunchSummaryDTO();
        summaryDTO.setDate(LocalDate.now().toString());
        summaryDTO.setEntryCount(2);
        summaryDTO.setExitCount(2);
        summaryDTO.setStatus("Completo");

        when(punchLogService.getPunchHistorySummary(userId)).thenReturn(List.of(summaryDTO));

        mockMvc.perform(get("/api/punch/history/summary/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(summaryDTO.getDate()))
                .andExpect(jsonPath("$[0].entryCount").value(summaryDTO.getEntryCount()))
                .andExpect(jsonPath("$[0].exitCount").value(summaryDTO.getExitCount()))
                .andExpect(jsonPath("$[0].status").value(summaryDTO.getStatus()));

        verify(punchLogService, times(1)).getPunchHistorySummary(userId);
    }

    @Test
    public void testGetPunchHistorySummary_NoContent() throws Exception {
        Long userId = 1L;

        when(punchLogService.getPunchHistorySummary(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/punch/history/summary/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(punchLogService, times(1)).getPunchHistorySummary(userId);
    }
}

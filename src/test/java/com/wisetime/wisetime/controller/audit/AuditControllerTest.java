package com.wisetime.wisetime.controller.audit;

import com.wisetime.wisetime.DTO.audit.AuditLogDTO;
import com.wisetime.wisetime.service.audit.AuditService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuditControllerTest {

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuditController auditController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController).build();
    }

    @Test
    public void testGetAuditLogs_Success() throws Exception {
        Long teamId = 1L;
        List<AuditLogDTO> auditLogs = List.of(
                new AuditLogDTO(1L, "Login", "User1", "User logged in", LocalDateTime.of(2023, 11, 1, 10, 15, 30)),
                new AuditLogDTO(2L, "Logout", "User2", "User logged out", LocalDateTime.of(2023, 11, 2, 11, 30, 45))
        );

        when(auditService.getAuditLogs(teamId)).thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit/logs/{teamId}", teamId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].action").value("Login"))
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[0].details").value("User logged in"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].action").value("Logout"))
                .andExpect(jsonPath("$[1].name").value("User2"))
                .andExpect(jsonPath("$[1].details").value("User logged out"));

        verify(auditService, times(1)).getAuditLogs(teamId);
    }

    @Test
    public void testGetAuditLogs_NoLogsFound() throws Exception {
        Long teamId = 1L;
        List<AuditLogDTO> emptyAuditLogs = new ArrayList<>();

        when(auditService.getAuditLogs(teamId)).thenReturn(emptyAuditLogs);

        mockMvc.perform(get("/api/audit/logs/{teamId}", teamId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(auditService, times(1)).getAuditLogs(teamId);
    }

    @Test
    public void testGetAuditLogs_Exception() throws Exception {
        Long teamId = 1L;

        when(auditService.getAuditLogs(teamId)).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(get("/api/audit/logs/{teamId}", teamId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(auditService, times(1)).getAuditLogs(teamId);
    }
}

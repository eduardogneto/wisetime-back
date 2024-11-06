package com.wisetime.wisetime.controller.duedate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import com.wisetime.wisetime.DTO.dueDateBank.DueDateBankDTO;
import com.wisetime.wisetime.controller.dueDateBank.DueDateBankController;
import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.dueDateBank.TagDueDateBankEnum;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;
import com.wisetime.wisetime.service.dueDateBank.DueDateBankService;
import com.wisetime.wisetime.service.organization.OrganizationService;

@ExtendWith(MockitoExtension.class)
public class DueDateBankControllerTest {

    @Mock
    private DueDateBankService dueDateBankService;

    @Mock
    private DueDateBankRepository dueDateBankRepository;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private DueDateBankController dueDateBankController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(dueDateBankController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetDueDateBanksByOrganization_Success() throws Exception {
        Long organizationId = 1L;
        Organization organization = new Organization();
        organization.setId(organizationId);
        DueDateBank dueDateBank = new DueDateBank(LocalDate.now(), LocalDate.now().plusDays(10), organization);

        when(dueDateBankService.findByOrganizationId(organizationId)).thenReturn(List.of(dueDateBank));

        mockMvc.perform(get("/api/dueDateBank/organization/{organizationId}/duedates", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organization.id").value(organizationId.intValue()));

        verify(dueDateBankService, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void testGetDueDateBanksByOrganization_NoContent() throws Exception {
        Long organizationId = 1L;

        when(dueDateBankService.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dueDateBank/organization/{organizationId}/duedates", organizationId))
                .andExpect(status().isNoContent());

        verify(dueDateBankService, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void testForceCreateNextDueDateBanks_Success() throws Exception {
        doNothing().when(dueDateBankService).createNextDueDateBanks();

        mockMvc.perform(post("/api/dueDateBank/forceCreate"))
                .andExpect(status().isOk())
                .andExpect(content().string("DueDateBanks foram atualizados com sucesso."));

        verify(dueDateBankService, times(1)).createNextDueDateBanks();
    }

    @Test
    public void testForceCreateNextDueDateBanks_Exception() throws Exception {
        doThrow(new RuntimeException("Failed to create DueDateBanks")).when(dueDateBankService).createNextDueDateBanks();

        mockMvc.perform(post("/api/dueDateBank/forceCreate"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro ao atualizar DueDateBanks: Failed to create DueDateBanks"));

        verify(dueDateBankService, times(1)).createNextDueDateBanks();
    }

    @Test
    public void testGetStartDateAndEndDateByOrganization_Success() throws Exception {
        Long organizationId = 1L;
        Organization organization = new Organization();
        organization.setId(organizationId);
        DueDateBank dueDateBank = new DueDateBank(LocalDate.now(), LocalDate.now().plusDays(10), organization);

        when(dueDateBankRepository.findByOrganizationId(organizationId)).thenReturn(List.of(dueDateBank));

        mockMvc.perform(get("/api/dueDateBank/periods/{organizationId}", organizationId))
                .andExpect(status().isOk());

        verify(dueDateBankRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void testGetStartDateAndEndDateByOrganization_NoContent() throws Exception {
        Long organizationId = 1L;

        when(dueDateBankRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dueDateBank/periods/{organizationId}", organizationId))
                .andExpect(status().isNoContent());

        verify(dueDateBankRepository, times(1)).findByOrganizationId(organizationId);
    }
}

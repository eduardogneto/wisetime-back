package com.wisetime.wisetime.controller.organization;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

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
import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.service.organization.OrganizationService;
import com.wisetime.wisetime.service.team.TeamService;

@ExtendWith(MockitoExtension.class)
public class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private OrganizationController organizationController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(organizationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllOrganizations() throws Exception {
        List<OrganizationDTO> organizations = Arrays.asList(
            new OrganizationDTO(1L, "Org 1", "12345", "org1@example.com", "123456789", null, null),
            new OrganizationDTO(2L, "Org 2", "67890", "org2@example.com", "987654321", null, null)
        );

        when(organizationService.getAllOrganizations()).thenReturn(organizations);

        mockMvc.perform(get("/api/organizations/allOrganizations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("Org 1"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("Org 2"));

        verify(organizationService, times(1)).getAllOrganizations();
    }

    @Test
    public void testGetOrganizationById_Found() throws Exception {
        Organization organization = new Organization();
        organization.setId(1L);
        OrganizationDTO organizationDTO = new OrganizationDTO(1L, "Org 1", "12345", "org1@example.com", "123456789", null, null);
        when(organizationService.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationService.convertToDTO(organization)).thenReturn(organizationDTO);

        mockMvc.perform(get("/api/organizations/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Org 1"));

        verify(organizationService, times(1)).findById(1L);
        verify(organizationService, times(1)).convertToDTO(organization);
    }

    @Test
    public void testGetOrganizationById_NotFound() throws Exception {
        when(organizationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/organizations/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Organização não encontrada"));

        verify(organizationService, times(1)).findById(1L);
    }

    @Test
    public void testCreateOrganization() throws Exception {
        OrganizationDTO organizationDTO = new OrganizationDTO(1L, "Org 1", "12345", "org1@example.com", "123456789", null, null);
        Organization organization = new Organization();
        when(organizationService.convertToEntity(organizationDTO)).thenReturn(organization);
        when(organizationService.convertToDTO(organization)).thenReturn(organizationDTO);

        mockMvc.perform(post("/api/organizations/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(organizationDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Org 1"));

        verify(organizationService, times(1)).convertToEntity(organizationDTO);
        verify(organizationService, times(1)).saveOrganization(organization, organizationDTO);
        verify(organizationService, times(1)).convertToDTO(organization);
    }

    @Test
    public void testUpdateOrganization_Found() throws Exception {
        OrganizationDTO organizationDTO = new OrganizationDTO(1L, "Org Updated", "12345", "org1@example.com", "123456789", null, null);
        Organization organization = new Organization();
        when(organizationService.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationService.updateOrganization(organization, organizationDTO)).thenReturn(organization);
        when(organizationService.convertToDTO(organization)).thenReturn(organizationDTO);

        mockMvc.perform(put("/api/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(organizationDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Org Updated"));

        verify(organizationService, times(1)).findById(1L);
        verify(organizationService, times(1)).updateOrganization(organization, organizationDTO);
        verify(organizationService, times(1)).saveOrganization(organization, organizationDTO);
        verify(organizationService, times(1)).convertToDTO(organization);
    }

    @Test
    public void testUpdateOrganization_NotFound() throws Exception {
        OrganizationDTO organizationDTO = new OrganizationDTO(null, null, null, null, null, null, null);
        when(organizationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(organizationDTO)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Organização não encontrada"));

        verify(organizationService, times(1)).findById(1L);
        verify(organizationService, never()).updateOrganization(any(), any());
    }

    @Test
    public void testGetTeamsByOrganization_Found() throws Exception {
        List<TeamDTO> teams = Arrays.asList(new TeamDTO(1L, "Team A", "Description A", 1L));
        when(teamService.getTeamsByOrganization(1L)).thenReturn(teams);

        mockMvc.perform(get("/api/organizations/1/teams"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("Team A"));

        verify(teamService, times(1)).getTeamsByOrganization(1L);
    }

    @Test
    public void testGetTeamsByOrganization_NotFound() throws Exception {
        when(teamService.getTeamsByOrganization(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/organizations/1/teams"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));

        verify(teamService, times(1)).getTeamsByOrganization(1L);
    }

    @Test
    public void testUpdateTeams_Success() throws Exception {
        List<TeamDTO> teamsDTO = Arrays.asList(new TeamDTO(1L, "Team Updated", "Description Updated", 1L));
        Organization organization = new Organization();
        when(organizationService.findById(1L)).thenReturn(Optional.of(organization));

        mockMvc.perform(put("/api/organizations/1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO)))
        .andExpect(content().string("Times atualizados com sucesso!"));

        verify(organizationService, times(1)).findById(1L);
    }

    @Test
    public void testUpdateTeams_OrganizationNotFound() throws Exception {
        List<TeamDTO> teamsDTO = new ArrayList<>();
        when(organizationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/organizations/1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Organização não encontrada"));

        verify(organizationService, times(1)).findById(1L);
        verify(organizationService, never()).updateTeams(any(), any());
    }

    @Test
    public void testUpdateTeams_Failure() throws Exception {
        List<TeamDTO> teamsDTO = new ArrayList<>();
        Organization organization = new Organization();
        when(organizationService.findById(1L)).thenReturn(Optional.of(organization));
        doThrow(new RuntimeException("Error")).when(organizationService).updateTeams(any(), any());

        mockMvc.perform(put("/api/organizations/1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Erro ao atualizar times"));

        verify(organizationService, times(1)).findById(1L);
        verify(organizationService, times(1)).updateTeams(organization, teamsDTO);
    }
}

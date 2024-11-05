package com.wisetime.wisetime.service.team;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.repository.team.TeamRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Organization organization;
    private Team team1;
    private Team team2;

    @BeforeEach
    public void setUp() {
        organization = new Organization();
        organization.setId(1L);

        team1 = new Team();
        team1.setId(1L);
        team1.setName("Team A");
        team1.setDescription("Description A");
        team1.setOrganization(organization);

        team2 = new Team();
        team2.setId(2L);
        team2.setName("Team B");
        team2.setDescription("Description B");
        team2.setOrganization(organization);
    }

    @Test
    public void testGetTeamsByOrganization_TeamsExist() {
        Long organizationId = organization.getId();
        List<Team> teams = Arrays.asList(team1, team2);

        when(teamRepository.findByOrganizationId(organizationId)).thenReturn(teams);

        List<TeamDTO> result = teamService.getTeamsByOrganization(organizationId);

        assertNotNull(result);
        assertEquals(2, result.size());

        TeamDTO teamDTO1 = result.get(0);
        assertEquals(team1.getId(), teamDTO1.getId());
        assertEquals(team1.getName(), teamDTO1.getName());
        assertEquals(team1.getDescription(), teamDTO1.getDescription());
        assertEquals(organizationId, teamDTO1.getOrganizationId());

        TeamDTO teamDTO2 = result.get(1);
        assertEquals(team2.getId(), teamDTO2.getId());
        assertEquals(team2.getName(), teamDTO2.getName());
        assertEquals(team2.getDescription(), teamDTO2.getDescription());
        assertEquals(organizationId, teamDTO2.getOrganizationId());

        verify(teamRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void testGetTeamsByOrganization_NoTeamsExist() {
        Long organizationId = organization.getId();

        when(teamRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        List<TeamDTO> result = teamService.getTeamsByOrganization(organizationId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(teamRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void testGetTeamsByOrganization_TeamWithoutOrganization() {
        Long organizationId = organization.getId();

        Team teamWithoutOrg = new Team();
        teamWithoutOrg.setId(3L);
        teamWithoutOrg.setName("Team C");
        teamWithoutOrg.setDescription("Description C");
        teamWithoutOrg.setOrganization(null);

        List<Team> teams = Arrays.asList(teamWithoutOrg);

        when(teamRepository.findByOrganizationId(organizationId)).thenReturn(teams);

        List<TeamDTO> result = teamService.getTeamsByOrganization(organizationId);

        assertNotNull(result);
        assertEquals(1, result.size());

        TeamDTO teamDTO = result.get(0);
        assertEquals(teamWithoutOrg.getId(), teamDTO.getId());
        assertEquals(teamWithoutOrg.getName(), teamDTO.getName());
        assertEquals(teamWithoutOrg.getDescription(), teamDTO.getDescription());
        assertNull(teamDTO.getOrganizationId());

        verify(teamRepository).findByOrganizationId(organizationId);
    }
}

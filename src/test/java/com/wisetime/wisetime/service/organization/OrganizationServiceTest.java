package com.wisetime.wisetime.service.organization;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.organzation.AddressDTO;
import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.service.audit.AuditService;
import com.wisetime.wisetime.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization organization;
    private OrganizationDTO organizationDTO;
    private Address address;
    private AddressDTO addressDTO;
    private Team team;
    private TeamDTO teamDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        address = new Address("Street", "123", "Apt 1", "City", "State", "Zip");
        addressDTO = new AddressDTO("Street", "123", "Apt 1", "City", "State", "Zip");

        organization = new Organization(1L, "Org Name", "TaxId", "org@example.com", "123456789", address);
        organization.setTeams(new ArrayList<>());

        organizationDTO = new OrganizationDTO(1L, "Org Name", "TaxId", "org@example.com", "123456789", addressDTO, new ArrayList<>());

        team = new Team("Team Name", "Team Description", organization);
        team.setId(1L);
        teamDTO = new TeamDTO(1L, "Team Name", "Team Description", organization.getId());

        user = new User();
        user.setId(1L);
        user.setName("Admin User");
        organizationDTO.setUserId(user.getId());
    }

    @Test
    public void testFindById_Found() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        Optional<Organization> result = organizationService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(organization, result.get());
        verify(organizationRepository).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Organization> result = organizationService.findById(1L);

        assertFalse(result.isPresent());
        verify(organizationRepository).findById(1L);
    }

    @Test
    public void testGetAllOrganizations() {
        List<Organization> organizations = Arrays.asList(organization);
        when(organizationRepository.findAll()).thenReturn(organizations);

        List<OrganizationDTO> result = organizationService.getAllOrganizations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(organizationDTO.getId(), result.get(0).getId());
        verify(organizationRepository).findAll();
    }

    @Test
    public void testConvertToDTO() {
        organization.setTeams(Arrays.asList(team));
        organizationDTO.setTeams(Arrays.asList(teamDTO));

        OrganizationDTO result = organizationService.convertToDTO(organization);

        assertNotNull(result);
        assertEquals(organizationDTO.getId(), result.getId());
        assertEquals(organizationDTO.getName(), result.getName());
        assertEquals(organizationDTO.getTaxId(), result.getTaxId());
        assertEquals(organizationDTO.getEmail(), result.getEmail());
        assertEquals(organizationDTO.getPhone(), result.getPhone());
        assertEquals(organizationDTO.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(organizationDTO.getTeams().size(), result.getTeams().size());
    }

    @Test
    public void testConvertToEntity() {
        organizationDTO.setTeams(Arrays.asList(teamDTO));

        Organization result = organizationService.convertToEntity(organizationDTO);

        assertNotNull(result);
        assertEquals(organizationDTO.getName(), result.getName());
        assertEquals(organizationDTO.getTaxId(), result.getTaxId());
        assertEquals(organizationDTO.getEmail(), result.getEmail());
        assertEquals(organizationDTO.getPhone(), result.getPhone());
        assertEquals(organizationDTO.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(organizationDTO.getTeams().size(), result.getTeams().size());
    }

    @Test
    public void testSaveOrganization_Success() {
        when(userService.findEntityById(anyLong())).thenReturn(Optional.of(user));
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        organizationService.saveOrganization(organization, organizationDTO);

        verify(organizationRepository).save(organization);
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testSaveOrganization_UserNotFound() {
        when(userService.findEntityById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            organizationService.saveOrganization(organization, organizationDTO);
        });

        assertEquals("Usuário não encontrado para o ID: " + organizationDTO.getUserId(), exception.getMessage());
        verify(organizationRepository, never()).save(any(Organization.class));
        verify(auditService, never()).logAction(anyString(), any(User.class), anyString());
    }

    @Test
    public void testUpdateOrganization_Success() {
        when(userService.findEntityById(anyLong())).thenReturn(Optional.of(user));

        Organization updatedOrganization = organizationService.updateOrganization(organization, organizationDTO);

        assertNotNull(updatedOrganization);
        assertEquals(organizationDTO.getName(), updatedOrganization.getName());
        assertEquals(organizationDTO.getTaxId(), updatedOrganization.getTaxId());
        assertEquals(organizationDTO.getEmail(), updatedOrganization.getEmail());
        assertEquals(organizationDTO.getPhone(), updatedOrganization.getPhone());
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testUpdateOrganization_UserNotFound() {
        when(userService.findEntityById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            organizationService.updateOrganization(organization, organizationDTO);
        });

        assertEquals("Usuário não encontrado para o ID: " + organizationDTO.getUserId(), exception.getMessage());
        verify(auditService, never()).logAction(anyString(), any(User.class), anyString());
    }

    @Test
    public void testUpdateTeams_AddAndUpdateTeams() {
        Team existingTeam1 = new Team("Existing Team 1", "Existing Description 1", organization);
        existingTeam1.setId(1L);
        Team existingTeam2 = new Team("Existing Team 2", "Existing Description 2", organization);
        existingTeam2.setId(2L);

        organization.setTeams(new ArrayList<>(Arrays.asList(existingTeam1, existingTeam2)));

        TeamDTO updatedTeamDTO = new TeamDTO(1L, "Updated Team 1", "Updated Description 1", organization.getId());
        TeamDTO newTeamDTO = new TeamDTO(null, "New Team", "New Description", organization.getId());

        List<TeamDTO> teamsDTO = Arrays.asList(updatedTeamDTO, newTeamDTO);

        organizationService.updateTeams(organization, teamsDTO);

        assertEquals(2, organization.getTeams().size());
        verify(teamRepository).delete(existingTeam2);
        verify(organizationRepository).save(organization);
    }

    @Test
    public void testUpdateTeams_RemoveAllTeams() {
        Team existingTeam = new Team("Existing Team", "Existing Description", organization);
        existingTeam.setId(1L);
        organization.setTeams(new ArrayList<>(Arrays.asList(existingTeam)));

        organizationService.updateTeams(organization, new ArrayList<>());

        assertEquals(0, organization.getTeams().size());
        verify(teamRepository).delete(existingTeam);
        verify(organizationRepository).save(organization);
    }

    @Test
    public void testUpdateTeams_NoChanges() {
        Team existingTeam = new Team("Existing Team", "Existing Description", organization);
        existingTeam.setId(1L);
        organization.setTeams(new ArrayList<>(Arrays.asList(existingTeam)));

        TeamDTO existingTeamDTO = new TeamDTO(1L, "Existing Team", "Existing Description", organization.getId());

        List<TeamDTO> teamsDTO = Arrays.asList(existingTeamDTO);

        organizationService.updateTeams(organization, teamsDTO);

        assertEquals(1, organization.getTeams().size());
        verify(teamRepository, never()).delete(any(Team.class));
        verify(organizationRepository).save(organization);
    }
}

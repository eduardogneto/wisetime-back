package com.wisetime.wisetime.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.TagUserEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    public void testFindEntityById_UserFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findEntityById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindEntityById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findEntityById(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUsersByOrganization_UsersFound() {
        Long organizationId = 1L;

        Organization org = new Organization();
        org.setId(organizationId);

        Team team = new Team();
        team.setId(2L);
        team.setOrganization(org);

        User user = new User();
        user.setId(3L);
        user.setTeam(team);

        List<User> users = Arrays.asList(user);

        when(userRepository.findByTeam_Organization_Id(organizationId)).thenReturn(users);

        List<UserResponseDTO> result = userService.getUsersByOrganization(organizationId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());

        verify(userRepository, times(1)).findByTeam_Organization_Id(organizationId);
    }

    @Test
    public void testGetUsersByOrganization_NoUsersFound() {
        Long organizationId = 1L;

        when(userRepository.findByTeam_Organization_Id(organizationId)).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.getUsersByOrganization(organizationId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByTeam_Organization_Id(organizationId);
    }

    @Test
    public void testMapToDTO_UserWithTeamAndOrganization() {
        Long userId = 1L;
        Long teamId = 2L;
        Long organizationId = 3L;

        Organization org = new Organization();
        org.setId(organizationId);

        Team team = new Team();
        team.setId(teamId);
        team.setName("Team Name");
        team.setDescription("Team Description");
        team.setOrganization(org);

        User user = new User();
        user.setId(userId);
        user.setName("User Name");
        user.setEmail("user@example.com");
        user.setTeam(team);
        user.setTag(TagUserEnum.FUNCIONARIO);

        UserResponseDTO dto = userService.mapToDTO(user);

        assertNotNull(dto);
        assertEquals(userId, dto.getId());
        assertEquals("User Name", dto.getName());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(TagUserEnum.FUNCIONARIO, dto.getTag());

        assertNotNull(dto.getTeam());
        assertEquals(teamId, dto.getTeam().getId());
        assertEquals("Team Name", dto.getTeam().getName());
        assertEquals("Team Description", dto.getTeam().getDescription());
        assertEquals(organizationId, dto.getTeam().getOrganizationId());
    }

    @Test
    public void testMapToDTO_UserWithTeamNoOrganization() {
        Long userId = 1L;
        Long teamId = 2L;

        Team team = new Team();
        team.setId(teamId);
        team.setName("Team Name");
        team.setDescription("Team Description");
        team.setOrganization(null);

        User user = new User();
        user.setId(userId);
        user.setName("User Name");
        user.setEmail("user@example.com");
        user.setTeam(team);
        user.setTag(TagUserEnum.FUNCIONARIO);

        UserResponseDTO dto = userService.mapToDTO(user);

        assertNotNull(dto);
        assertEquals(userId, dto.getId());
        assertEquals("User Name", dto.getName());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(TagUserEnum.FUNCIONARIO, dto.getTag());

        assertNotNull(dto.getTeam());
        assertEquals(teamId, dto.getTeam().getId());
        assertEquals("Team Name", dto.getTeam().getName());
        assertEquals("Team Description", dto.getTeam().getDescription());
        assertNull(dto.getTeam().getOrganizationId());
    }

    @Test
    public void testMapToDTO_UserWithoutTeam() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("User Name");
        user.setEmail("user@example.com");
        user.setTeam(null);
        user.setTag(TagUserEnum.FUNCIONARIO);

        UserResponseDTO dto = userService.mapToDTO(user);

        assertNotNull(dto);
        assertEquals(userId, dto.getId());
        assertEquals("User Name", dto.getName());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(TagUserEnum.FUNCIONARIO, dto.getTag());

        assertNull(dto.getTeam());
    }

    @Test
    public void testFindAllUsers_UsersExist() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllUsers_NoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployees_ValidOrganizationId() {
        Long organizationId = 1L;
        Long count = 5L;

        when(userRepository.countByOrganizationId(organizationId)).thenReturn(count);

        Long result = userService.getEmployees(organizationId);

        assertEquals(count, result);
        verify(userRepository, times(1)).countByOrganizationId(organizationId);
    }

    @Test
    public void testGetEmployees_NullOrganizationId() {
        Long result = userService.getEmployees(null);

        assertEquals(0L, result);
        verify(userRepository, never()).countByOrganizationId(anyLong());
    }

    @Test
    public void testGetEmployees_CountIsNull() {
        Long organizationId = 1L;

        when(userRepository.countByOrganizationId(organizationId)).thenReturn(null);

        Long result = userService.getEmployees(organizationId);

        assertEquals(0L, result);
        verify(userRepository, times(1)).countByOrganizationId(organizationId);
    }

    @Test
    public void testGetEmployees_NoEmployees() {
        Long organizationId = 1L;
        Long count = 0L;

        when(userRepository.countByOrganizationId(organizationId)).thenReturn(count);

        Long result = userService.getEmployees(organizationId);

        assertEquals(count, result);
        verify(userRepository, times(1)).countByOrganizationId(organizationId);
    }

    @Test
    public void testDeleteUsersByIds_AllUsersExist() {
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);

        when(userRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<String> response = userService.deleteUsersByIds(userIds);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("deletados com sucesso"));

        verify(userRepository, times(userIds.size())).existsById(anyLong());
        verify(userRepository, times(userIds.size())).deleteById(anyLong());
    }

    @Test
    public void testDeleteUsersByIds_SomeUsersDoNotExist() {
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);
        when(userRepository.existsById(3L)).thenReturn(true);

        ResponseEntity<String> response = userService.deleteUsersByIds(userIds);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Usuários não encontrados para os IDs: [2]"));

        verify(userRepository, times(userIds.size())).existsById(anyLong());
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteUsersByIds_NoUsersExist() {
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);

        when(userRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<String> response = userService.deleteUsersByIds(userIds);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Usuários não encontrados para os IDs: [1, 2, 3]"));

        verify(userRepository, times(userIds.size())).existsById(anyLong());
        verify(userRepository, never()).deleteById(anyLong());
    }
}

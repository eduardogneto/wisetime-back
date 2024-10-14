package com.wisetime.wisetime.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.TagUserEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.user.UserRepository;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testFindEntityById_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Eduardo");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals("Eduardo", result.get().getName());
    }

    @Test
    void testFindEntityById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findEntityById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetUsersByOrganization() {
        Organization organization = new Organization();
        organization.setId(1L);

        Team team = new Team();
        team.setId(1L);
        team.setName("Dev Team");
        team.setDescription("Equipe de desenvolvimento");
        team.setOrganization(organization);

        User user = new User();
        user.setId(1L);
        user.setName("Eduardo");
        user.setEmail("eduardo@wisetime.com");
        user.setTeam(team);
        user.setTag(TagUserEnum.valueOf("ADMINISTRADOR"));

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByTeam_Organization_Id(1L)).thenReturn(users);

        List<UserResponseDTO> result = userService.getUsersByOrganization(1L);

        assertEquals(1, result.size());
        UserResponseDTO dto = result.get(0);
        assertEquals("Eduardo", dto.getName());
        assertEquals("Dev Team", dto.getTeam().getName());
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Eduardo");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("João");

        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("Eduardo", result.get(0).getName());
        assertEquals("João", result.get(1).getName());
    }
}

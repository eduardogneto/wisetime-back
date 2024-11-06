package com.wisetime.wisetime.controller.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetime.wisetime.DTO.balance.BalanceDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.balance.BalanceService;
import com.wisetime.wisetime.service.team.TeamService;
import com.wisetime.wisetime.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TeamService teamService;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserBalances_Success() throws Exception {
        Long userId = 1L;
        BalanceDTO balanceDTO = new BalanceDTO();

        when(balanceService.getUserBalancesFromDatabase(userId)).thenReturn(balanceDTO);

        mockMvc.perform(get("/api/users/{userId}/balances", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(balanceDTO)));

        verify(balanceService, times(1)).getUserBalancesFromDatabase(userId);
    }

    @Test
    public void testCalculateUserBalances_Success() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.findEntityById(userId)).thenReturn(Optional.of(user));
        doNothing().when(balanceService).calculateAndSaveUserBalance(user);

        mockMvc.perform(post("/api/users/{userId}/calculate-balances", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Cálculo dos saldos realizado com sucesso para o usuário ID: " + userId));

        verify(userService, times(1)).findEntityById(userId);
        verify(balanceService, times(1)).calculateAndSaveUserBalance(user);
    }

    @Test
    public void testGetUsersByOrganization_Success() throws Exception {
        Long organizationId = 1L;
        List<UserResponseDTO> userDTOs = List.of(new UserResponseDTO());

        when(userService.getUsersByOrganization(organizationId)).thenReturn(userDTOs);

        mockMvc.perform(get("/api/users/organization")
                .param("organizationId", organizationId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTOs)));

        verify(userService, times(1)).getUsersByOrganization(organizationId);
    }

    @Test
    public void testGetTeamsByOrganization_Success() throws Exception {
        Long organizationId = 1L;
        List<TeamDTO> teamDTOs = List.of(new TeamDTO());

        when(teamService.getTeamsByOrganization(organizationId)).thenReturn(teamDTOs);

        mockMvc.perform(get("/api/users/teams")
                .param("organizationId", organizationId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teamDTOs)));

        verify(teamService, times(1)).getTeamsByOrganization(organizationId);
    }

    @Test
    public void testGetEmployees_Success() throws Exception {
        Long organizationId = 1L;
        Long employeeCount = 5L;

        when(userService.getEmployees(organizationId)).thenReturn(employeeCount);

        mockMvc.perform(get("/api/users/employees/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().string(employeeCount.toString()));

        verify(userService, times(1)).getEmployees(organizationId);
    }

    @Test
    public void testDeleteUsers_Success() throws Exception {
        List<Long> userIds = List.of(1L, 2L);

        when(userService.deleteUsersByIds(userIds)).thenReturn(ResponseEntity.ok("Usuários deletados com sucesso."));

        mockMvc.perform(delete("/api/users/deleteUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuários deletados com sucesso."));

        verify(userService, times(1)).deleteUsersByIds(userIds);
    }

    @Test
    public void testDeleteUsers_PartialFailure() throws Exception {
        List<Long> userIds = List.of(1L, 2L);

        when(userService.deleteUsersByIds(userIds)).thenReturn(ResponseEntity.status(500).body("Falha ao deletar alguns usuários."));

        mockMvc.perform(delete("/api/users/deleteUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Falha ao deletar alguns usuários."));

        verify(userService, times(1)).deleteUsersByIds(userIds);
    }
}

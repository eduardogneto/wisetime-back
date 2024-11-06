package com.wisetime.wisetime.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetime.wisetime.DTO.auth.AuthenticationDTO;
import com.wisetime.wisetime.DTO.auth.RegisterDTO;
import com.wisetime.wisetime.DTO.user.EditUserDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.infra.security.TokenService;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.TagUserEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.repository.user.UserRepository;
import com.wisetime.wisetime.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testLogin_Success() throws Exception {
        AuthenticationDTO authData = new AuthenticationDTO("user@example.com", "password");
        User user = new User();
        user.setEmail(authData.getEmail());
        user.setPassword(authData.getPassword());
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        String token = "test-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));
        when(tokenService.generateToken(user)).thenReturn(token);
        when(userService.mapToDTO(user)).thenReturn(userResponseDTO);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(user);
        verify(userService, times(1)).mapToDTO(user);
    }

    @Test
    public void testLogin_BadCredentials() throws Exception {
        AuthenticationDTO authData = new AuthenticationDTO("user@example.com", "wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authData)))
                .andExpect(status().isBadRequest());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRegister_Success() throws Exception {
        RegisterDTO registerData = new RegisterDTO("newuser@example.com", "newpassword", TagUserEnum.ADMINISTRADOR, "New User", 1L);
        User newUser = new User();
        newUser.setEmail(registerData.getEmail());

        Team team = new Team();
        team.setId(registerData.getTeamId());

        when(userRepository.findByEmail(registerData.getEmail())).thenReturn(null);
        when(teamRepository.findById(registerData.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerData)))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).findByEmail(registerData.getEmail());
        verify(teamRepository, times(1)).findById(registerData.getTeamId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegister_UserAlreadyExists() throws Exception {
        RegisterDTO registerData = new RegisterDTO("existinguser@example.com", "password", TagUserEnum.ADMINISTRADOR, "Existing User", 1L);

        when(userRepository.findByEmail(registerData.getEmail())).thenReturn(new User());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerData)))
                .andExpect(status().isBadRequest());

        verify(userRepository, times(1)).findByEmail(registerData.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testEditUser_Success() throws Exception {
        Long userId = 1L;
        EditUserDTO editData = new EditUserDTO("Existing User", "existinguser@example.com", "password", TagUserEnum.ADMINISTRADOR, 1L);
        User user = new User();
        user.setId(userId);
        user.setEmail(editData.getEmail());

        Team team = new Team();
        team.setId(editData.getTeamId());

        when(userRepository.findByEmail(editData.getEmail())).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(teamRepository.findById(editData.getTeamId())).thenReturn(Optional.of(team));

        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "Existing User", "existinguser@example.com", null, TagUserEnum.ADMINISTRADOR);
        when(userService.mapToDTO(user)).thenReturn(userResponseDTO);

        mockMvc.perform(put("/auth/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Existing User"))
                .andExpect(jsonPath("$.email").value("existinguser@example.com"))
                .andExpect(jsonPath("$.tag").value("ADMINISTRADOR"));

        verify(userRepository, times(1)).findByEmail(editData.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testValidateToken_Success() throws Exception {
        String token = "Bearer valid-token";
        String subject = "user@example.com";

        when(tokenService.validateToken("valid-token")).thenReturn(subject);

        mockMvc.perform(get("/auth/validate-token")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));

        verify(tokenService, times(1)).validateToken("valid-token");
    }

    @Test
    public void testValidateToken_Invalid() throws Exception {
        String token = "Bearer invalid-token";

        when(tokenService.validateToken("invalid-token")).thenReturn("");

        mockMvc.perform(get("/auth/validate-token")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token is invalid"));

        verify(tokenService, times(1)).validateToken("invalid-token");
    }
}

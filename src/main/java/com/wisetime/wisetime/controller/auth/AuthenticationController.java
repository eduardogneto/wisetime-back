package com.wisetime.wisetime.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.auth.AuthenticationDTO;
import com.wisetime.wisetime.DTO.auth.LoginResponseDTO;
import com.wisetime.wisetime.DTO.auth.RegisterDTO;
import com.wisetime.wisetime.DTO.user.EditUserDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.infra.security.TokenService;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.repository.user.UserRepository;
import com.wisetime.wisetime.service.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager; 
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());

            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();

            var token = tokenService.generateToken(user);

            UserResponseDTO userResponseDTO = userService.mapToDTO(user);

            return ResponseEntity.ok(new LoginResponseDTO(token, userResponseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RuntimeException("E-mail ou senha incorretos."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RuntimeException("Erro interno de autenticação."));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
    	if(this.userRepository.findByEmail(data.getEmail()) != null) return ResponseEntity.badRequest().build();
    	
    	String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
    	
    	User newUser = new User();
    	newUser.setName(data.getName()); 
    	newUser.setEmail(data.getEmail());
    	newUser.setPassword(encryptedPassword);
    	newUser.setTag(data.getTag());

    	Team team = teamRepository.findById(data.getTeamId())
    	    .orElseThrow(() -> new RuntimeException("Time não encontrado para o ID: " + data.getTeamId()));
    	newUser.setTeam(team);
    	
    	this.userRepository.save(newUser);
    	
    	return ResponseEntity.ok().build();	
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody @Valid EditUserDTO data) {
        try {
            User existingUserWithEmail = (User) userRepository.findByEmail(data.getEmail());
            if (existingUserWithEmail != null && !existingUserWithEmail.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
            }

            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + id));

            user.setName(data.getName());
            user.setEmail(data.getEmail());
            user.setTag(data.getTag());

            Team team = teamRepository.findById(data.getTeamId())
                .orElseThrow(() -> new RuntimeException("Time não encontrado para o ID: " + data.getTeamId()));
            user.setTeam(team);

            if(data.getPassword() != null && !data.getPassword().isEmpty()) {
                String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
                user.setPassword(encryptedPassword);
            }

            userRepository.save(user);

            UserResponseDTO userResponseDTO = userService.mapToDTO(user);

            return ResponseEntity.ok(userResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar usuário.");
        }
    }
    
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            String subject = tokenService.validateToken(token);

            if (!subject.isEmpty()) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }
    }
    
}

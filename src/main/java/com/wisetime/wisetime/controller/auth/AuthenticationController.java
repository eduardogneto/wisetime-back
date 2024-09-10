package com.wisetime.wisetime.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.auth.AuthenticationDTO;
import com.wisetime.wisetime.DTO.auth.LoginResponseDTO;
import com.wisetime.wisetime.DTO.auth.RegisterDTO;
import com.wisetime.wisetime.infra.security.TokenService;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.repository.user.UserRepository;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        
        var auth = authenticationManager.authenticate(usernamePassword);
        
        var token = tokenService.generateToken((User) auth.getPrincipal());
        
        return ResponseEntity.ok( new LoginResponseDTO(token));
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

}

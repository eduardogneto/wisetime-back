package com.wisetime.wisetime.controller.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.DTO.user.UserLoginDTO;
import com.wisetime.wisetime.DTO.user.UserRegisterDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.service.team.TeamService;
import com.wisetime.wisetime.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

//    @PostMapping("/register")
//    public ResponseEntity<?> registerOrUpdate(@RequestBody UserRegisterDTO registerDTO) {
//        try {
//            UserResponseDTO savedUserDTO = userService.registerOrUpdate(registerDTO);
//            return ResponseEntity.ok(savedUserDTO);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
//        Optional<UserResponseDTO> userDTOOpt = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
//
//        if (userDTOOpt.isPresent()) {
//            return ResponseEntity.ok(userDTOOpt.get());
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ou senha inválidos");
//        }
//    }

    @GetMapping("/organization")
    public ResponseEntity<List<UserResponseDTO>> getUsersByOrganization(@RequestParam Long organizationId) {
        List<UserResponseDTO> userDTOs = userService.getUsersByOrganization(organizationId);
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeamsByOrganization(@RequestParam Long organizationId) {
        List<TeamDTO> teamDTOs = teamService.getTeamsByOrganization(organizationId);
        return ResponseEntity.ok(teamDTOs);
    }
}


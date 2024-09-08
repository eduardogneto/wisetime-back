package com.wisetime.wisetime.controller.user;

import com.wisetime.wisetime.DTO.user.UserLoginDTO;
import com.wisetime.wisetime.DTO.user.UserRegisterDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.role.Role;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.role.RoleRepository;
import com.wisetime.wisetime.service.role.RoleService;
import com.wisetime.wisetime.service.user.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> registerOrUpdate(@RequestBody UserRegisterDTO registerDTO) {
        try {

            User user = new User();
            user.setId(registerDTO.getId()); 
            user.setName(registerDTO.getName());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(registerDTO.getPassword());
            user.setTag(registerDTO.getTag());

            Role role = roleRepository.findById(registerDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Nenhum cargo encontrado para o id: " + registerDTO.getRoleId()));

            user.setRole(role);

            User savedUser = userService.saveOrUpdateUser(user);
            return ResponseEntity.ok(savedUser);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        Optional<User> userOpt = userService.login(loginDTO.getEmail(), loginDTO.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().getOrganization().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ou senha inválidos");
        }
    }
    
    @GetMapping("/organization")
    public ResponseEntity<List<User>> getUsersByOrganization(@RequestParam Long organizationId) {
        List<User> users = userService.getUsersByOrganization(organizationId);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRolesByOrganization(@RequestParam Long organizationId) {
        List<Role> roles = roleService.getRolesByOrganization(organizationId);
        return ResponseEntity.ok(roles);
    }
    
    
}

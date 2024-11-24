package com.wisetime.wisetime.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.balance.BalanceDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.service.audit.AuditService;
import com.wisetime.wisetime.service.balance.BalanceService;
import com.wisetime.wisetime.service.team.TeamService;
import com.wisetime.wisetime.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private BalanceService balanceService;

    @GetMapping("/{userId}/balances")
    public BalanceDTO getUserBalances(@PathVariable Long userId) {
        return balanceService.getUserBalancesFromDatabase(userId);
    }
    
    @PostMapping("/{userId}/calculate-balances")
    public String calculateUserBalances(@PathVariable Long userId) {
        User user = userService.findEntityById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + userId));
        balanceService.calculateAndSaveUserBalance(user);
        auditService.logAction("Calculo de Horas", user, "O Usuário ativou o calculo de horas!");
        return "Cálculo dos saldos realizado com sucesso para o usuário ID: " + userId;
    }

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
    
    @GetMapping("/employees/{organizationId}")
    public ResponseEntity<Long> getEmployees(@PathVariable Long organizationId) {
    	Long count = userService.getEmployees(organizationId);
    	return ResponseEntity.ok(count);
    }
    
    @DeleteMapping("/deleteUsers")
    public ResponseEntity<String> deleteUsers(@RequestBody List<Long> userIds) {
        return userService.deleteUsersByIds(userIds);
    }
}


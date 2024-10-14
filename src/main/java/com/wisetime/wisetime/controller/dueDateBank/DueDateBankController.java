package com.wisetime.wisetime.controller.dueDateBank;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.dueDateBank.DueDateBankDTO;
import com.wisetime.wisetime.DTO.period.PeriodDTO;
import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;
import com.wisetime.wisetime.service.dueDateBank.DueDateBankService;
import com.wisetime.wisetime.service.organization.OrganizationService;

@RestController
@RequestMapping("/api/dueDateBank")
public class DueDateBankController {

    @Autowired
    private DueDateBankService dueDateBankService;
    
    @Autowired
    private DueDateBankRepository dueDateBankRepository;

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/create")
    public ResponseEntity<DueDateBank> createDueDateBank(@RequestBody DueDateBankDTO dueDateBankDTO) {

        Organization organization = organizationService.findById(dueDateBankDTO.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organização não encontrada para o ID: " + dueDateBankDTO.getOrganizationId()));

        DueDateBank dueDateBank = new DueDateBank(
                dueDateBankDTO.getStartDate(),
                dueDateBankDTO.getEndDate(),
                organization);

        DueDateBank savedDueDateBank = dueDateBankService.saveDueDateBank(dueDateBank);

        return ResponseEntity.ok(savedDueDateBank);
    }

    @GetMapping("/organization/{organizationId}/duedates")
    public ResponseEntity<List<DueDateBank>> getDueDateBanksByOrganization(@PathVariable Long organizationId) {
        List<DueDateBank> dueDateBanks = dueDateBankService.findByOrganizationId(organizationId);
        
        if (dueDateBanks.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        
        return ResponseEntity.ok(dueDateBanks); 
    }
    
    @PostMapping("/forceCreate")
    public ResponseEntity<String> forceCreateNextDueDateBanks() {
        try {
            dueDateBankService.createNextDueDateBanks();
            return ResponseEntity.ok("DueDateBanks foram atualizados com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar DueDateBanks: " + e.getMessage());
        }
    }
    
    @GetMapping("/periods/{organizationId}")
    public ResponseEntity<List<PeriodDTO>> getStartDateAndEndDateByOrganization(@PathVariable Long organizationId) {
        List<DueDateBank> dueDateBanks = dueDateBankRepository.findByOrganizationId(organizationId);

        if (dueDateBanks.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }

        List<PeriodDTO> periodDTOs = dueDateBanks.stream()
            .map(dueDateBank -> new PeriodDTO(dueDateBank.getId(), dueDateBank.getStartDate(), dueDateBank.getEndDate()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(periodDTOs);
    }
    
}

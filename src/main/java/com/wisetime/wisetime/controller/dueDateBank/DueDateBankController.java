package com.wisetime.wisetime.controller.dueDateBank;

import com.wisetime.wisetime.DTO.dueDateBank.DueDateBankDTO;
import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.service.dueDateBank.DueDateBankService;
import com.wisetime.wisetime.service.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dueDateBank")
public class DueDateBankController {

    @Autowired
    private DueDateBankService dueDateBankService;

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
}

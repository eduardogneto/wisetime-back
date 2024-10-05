package com.wisetime.wisetime.service.dueDateBank;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;

@Service
public class DueDateBankService {

    @Autowired
    private DueDateBankRepository dueDateBankRepository;


    public List<DueDateBank> findByOrganizationId(Long organizationId) {
        List<DueDateBank> dueDateBanks = dueDateBankRepository.findByOrganizationId(organizationId);
        
        // Força o carregamento da organização para cada DueDateBank
        dueDateBanks.forEach(dueDateBank -> {
            Organization org = dueDateBank.getOrganization();
            if (org != null) {
                org.getName(); // Acessa um campo da organização para garantir o carregamento
            }
        });
        
        return dueDateBanks;
    }
    
    public List<DueDateBank> findAllCurrentDueDateBanks() {
        LocalDate today = LocalDate.now();
        return dueDateBankRepository.findByEndDateAfter(today);
    }

    public boolean existsByStartDateAndOrganization(LocalDate startDate, Organization organization) {
        return dueDateBankRepository.existsByStartDateAndOrganization(startDate, organization);
    }
    
    public DueDateBank saveDueDateBank(DueDateBank dueDateBank) {
        return dueDateBankRepository.save(dueDateBank);
    }
    
}

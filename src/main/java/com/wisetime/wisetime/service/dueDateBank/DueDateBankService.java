package com.wisetime.wisetime.service.dueDateBank;


import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DueDateBankService {

    @Autowired
    private DueDateBankRepository dueDateBankRepository;

    public DueDateBank saveDueDateBank(DueDateBank dueDateBank) {
        return dueDateBankRepository.save(dueDateBank);
    }
    
    

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
}

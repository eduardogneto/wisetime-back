package com.wisetime.wisetime.service.dueDateBank;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;

@Service
public class DueDateBankService {
	
	private static final Logger logger = LoggerFactory.getLogger(DueDateBankScheduler.class);

    @Autowired
    private DueDateBankRepository dueDateBankRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;


    public List<DueDateBank> findByOrganizationId(Long organizationId) {
        List<DueDateBank> dueDateBanks = dueDateBankRepository.findByOrganizationId(organizationId);
        
        dueDateBanks.forEach(dueDateBank -> {
            Organization org = dueDateBank.getOrganization();
            if (org != null) {
                org.getName();
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
    
    public DueDateBank getCurrentDueDateBank(LocalDate date, Long organizationId) {
        return dueDateBankRepository.findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                organizationId, date, date);
    }

    public DueDateBank getPreviousDueDateBank(LocalDate date, Long organizationId) {
        return dueDateBankRepository.findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
                organizationId, date);
    }
    
    public DueDateBank getEarliestDueDateBank(Long organizationId) {
        return dueDateBankRepository.findTopByOrganizationIdOrderByStartDateAsc(organizationId);
    }
    
    public void createNextDueDateBanks() {
        logger.info("Iniciando o método createNextDueDateBanks");

        List<DueDateBank> currentDueDateBanks = findAllCurrentDueDateBanks();

        if (currentDueDateBanks == null || currentDueDateBanks.isEmpty()) {

            List<Organization> organizations = organizationRepository.findAll();
            for (Organization org : organizations) {
                DueDateBank newDueDateBank = new DueDateBank(
                        LocalDate.now(),
                        LocalDate.now().minusDays(1).plusMonths(1),
                        org
                );
                saveDueDateBank(newDueDateBank);
            }
        } else {

            for (DueDateBank dueDateBank : currentDueDateBanks) {
                LocalDate today = LocalDate.now();
                LocalDate endDate = dueDateBank.getEndDate();
                long daysUntilEnd = ChronoUnit.DAYS.between(today, endDate);


                if (daysUntilEnd <= 5 && daysUntilEnd >= 0) {

                    boolean nextPeriodExists = existsByStartDateAndOrganization(
                            dueDateBank.getEndDate().plusDays(1), dueDateBank.getOrganization());


                    if (!nextPeriodExists) {
                        DueDateBank nextDueDateBank = new DueDateBank(
                                dueDateBank.getEndDate().plusDays(1),
                                dueDateBank.getEndDate().plusMonths(1),
                                dueDateBank.getOrganization()
                        );
                        saveDueDateBank(nextDueDateBank);
                    } 
                } 
            }
        }

        logger.info("Finalizou o método createNextDueDateBanks");
    }
    
}

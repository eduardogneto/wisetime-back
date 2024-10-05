package com.wisetime.wisetime.service.dueDateBank;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;

@Service
public class DueDateBankScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DueDateBankScheduler.class);

    @Autowired
    private DueDateBankService dueDateBankService;

    @Scheduled(cron = "0 0 0 * * ?") // Executa a cada minuto para testes
    public void createNextDueDateBanks() {
        logger.info("Iniciando o método agendado createNextDueDateBanks");

        List<DueDateBank> currentDueDateBanks = dueDateBankService.findAllCurrentDueDateBanks();
        logger.info("DueDateBanks atuais encontrados: {}", currentDueDateBanks.size());

        for (DueDateBank dueDateBank : currentDueDateBanks) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = dueDateBank.getEndDate();
            long daysUntilEnd = ChronoUnit.DAYS.between(today, endDate);

            logger.info("Analisando DueDateBank ID {}: termina em {}, faltam {} dias", dueDateBank.getId(), endDate, daysUntilEnd);

            // Se faltam 5 dias ou menos para o término
            if (daysUntilEnd <= 5 && daysUntilEnd >= 0) {
                logger.info("DueDateBank ID {} está nos últimos 5 dias", dueDateBank.getId());

                // Verifica se já existe o próximo período
                boolean nextPeriodExists = dueDateBankService.existsByStartDateAndOrganization(
                        dueDateBank.getEndDate(), dueDateBank.getOrganization());

                logger.info("Próximo período já existe para a organização {}: {}", dueDateBank.getOrganization().getId(), nextPeriodExists);

                if (!nextPeriodExists) {
                    // Cria o próximo período
                    DueDateBank nextDueDateBank = new DueDateBank(
                            dueDateBank.getEndDate(),
                            dueDateBank.getEndDate().plusMonths(1),
                            dueDateBank.getOrganization()
                    );
                    dueDateBankService.saveDueDateBank(nextDueDateBank);
                    logger.info("Criado novo DueDateBank para a organização {} com início em {}", dueDateBank.getOrganization().getId(), nextDueDateBank.getStartDate());
                } else {
                    logger.info("Já existe um DueDateBank futuro para a organização {}", dueDateBank.getOrganization().getId());
                }
            } else {
                logger.info("DueDateBank ID {} não está nos últimos 5 dias", dueDateBank.getId());
            }
        }

        logger.info("Finalizou o método agendado createNextDueDateBanks");
    }
}

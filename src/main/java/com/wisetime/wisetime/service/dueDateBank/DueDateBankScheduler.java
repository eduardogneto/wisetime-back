package com.wisetime.wisetime.service.dueDateBank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DueDateBankScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DueDateBankScheduler.class);

    @Autowired
    private DueDateBankService dueDateBankService;

    @Scheduled(cron = "0 0 0 * * ?") 
    public void createNextDueDateBanks() {
        logger.info("Iniciando o método agendado createNextDueDateBanks");
        dueDateBankService.createNextDueDateBanks();
    }
}

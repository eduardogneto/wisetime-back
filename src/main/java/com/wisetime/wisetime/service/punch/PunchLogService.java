package com.wisetime.wisetime.service.punch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.repository.punch.PunchLogRepository;

@Service
public class PunchLogService {

    @Autowired
    private PunchLogRepository punchLogRepository;

    public PunchLog save(PunchLog punchLog) {
        return punchLogRepository.save(punchLog);
    }

    public PunchLog findById(Long punchLogId) {
        return punchLogRepository.findById(punchLogId)
                .orElseThrow(() -> new RuntimeException("Batida de ponto não encontrada"));
    }

    public List<PunchLog> getPunchLogsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return punchLogRepository.findByUserIdAndTimestampBetween(userId, start, end);
    }

    public List<PunchLog> getPunchLogsByUser(Long userId) {
        return punchLogRepository.findByUserId(userId);
    }

    public Optional<PunchLog> getLastPunchLog(Long userId) {
        List<PunchLog> logs = punchLogRepository.findLastPunchLogByUserId(userId);
        return logs.isEmpty() ? Optional.empty() : Optional.of(logs.get(0));
    }

    // Ajustar as batidas subsequentes para manter o padrão de alternância
    public void adjustSubsequentPunchLogs(Long userId, LocalDateTime startTimestamp, PunchTypeEnum startingType) {
        // Buscar todas as batidas subsequentes a partir do registro alterado
        List<PunchLog> subsequentLogs = punchLogRepository.findSubsequentPunchLogs(userId, startTimestamp);

        PunchTypeEnum nextType = startingType == PunchTypeEnum.ENTRY ? PunchTypeEnum.EXIT : PunchTypeEnum.ENTRY;

        // Ajustar o tipo de cada batida subsequente, alternando entre Entrada e Saída
        for (PunchLog log : subsequentLogs) {
            log.setType(nextType);
            nextType = nextType == PunchTypeEnum.ENTRY ? PunchTypeEnum.EXIT : PunchTypeEnum.ENTRY;
            punchLogRepository.save(log); // Salvar o ajuste
        }
    }
    
    public List<PunchLog> findByUserId(Long userId) {
        return punchLogRepository.findByUserId(userId);
    }
}


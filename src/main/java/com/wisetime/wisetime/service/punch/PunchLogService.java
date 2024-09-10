package com.wisetime.wisetime.service.punch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.punch.PunchLogRepository;
import com.wisetime.wisetime.service.user.UserService;

@Service
public class PunchLogService {

    @Autowired
    private PunchLogRepository punchLogRepository;
    
    @Autowired
    private UserService userService;

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

    public void adjustSubsequentPunchLogs(Long userId, LocalDateTime startTimestamp, PunchTypeEnum startingType) {
        List<PunchLog> subsequentLogs = punchLogRepository.findSubsequentPunchLogs(userId, startTimestamp);

        PunchTypeEnum nextType = startingType == PunchTypeEnum.ENTRY ? PunchTypeEnum.EXIT : PunchTypeEnum.ENTRY;

        for (PunchLog log : subsequentLogs) {
            log.setType(nextType);
            nextType = nextType == PunchTypeEnum.ENTRY ? PunchTypeEnum.EXIT : PunchTypeEnum.ENTRY;
            punchLogRepository.save(log); 
        }
    }
    
    public List<PunchLog> findByUserId(Long userId) {
        return punchLogRepository.findByUserId(userId);
    }
    
    public PunchLog logPunch(PunchLogDTO punchLogDTO) {
    	User user = userService.findEntityById(punchLogDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + punchLogDTO.getUserId()));

        Optional<PunchLog> lastPunchOpt = getLastPunchLog(user.getId());

        PunchLog punchLog = new PunchLog();
        punchLog.setUser(user);
        punchLog.setTimestamp(punchLogDTO.getTimestamp());
        punchLog.setLocation(punchLogDTO.getLocation());
        punchLog.setOrganization(user.getTeam().getOrganization());

        if (lastPunchOpt.isPresent()) {
            PunchLog lastPunch = lastPunchOpt.get();
            PunchTypeEnum lastPunchType = lastPunch.getType();

            punchLog.setType(lastPunchType == PunchTypeEnum.ENTRY ? PunchTypeEnum.EXIT : PunchTypeEnum.ENTRY);
        } else {
            punchLog.setType(PunchTypeEnum.ENTRY);  
        }

        return save(punchLog);
    }
    
    public List<PunchLog> getPunchHistoryForDay(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getPunchLogsByUserAndDateRange(userId, startOfDay, endOfDay);
    }
    
    public List<PunchSummaryDTO> getPunchHistorySummary(Long userId) {
        List<PunchLog> punchLogs = getPunchLogsByUser(userId);

        Map<String, PunchSummaryDTO> punchSummaryMap = new HashMap<>();

        for (PunchLog punch : punchLogs) {
            String date = punch.getTimestamp().toLocalDate().toString();
            PunchSummaryDTO summary = punchSummaryMap.getOrDefault(date, new PunchSummaryDTO());
            summary.setDate(date);

            if (punch.getType() == PunchTypeEnum.ENTRY) {
                summary.setEntryCount(summary.getEntryCount() + 1);
            } else {
                summary.setExitCount(summary.getExitCount() + 1);
            }

            summary.setStatus(summary.getEntryCount() == summary.getExitCount() ? "Completo" : "Incompleto");

            punchSummaryMap.put(date, summary);
        }

        return punchSummaryMap.values().stream().collect(Collectors.toList());
    }




}


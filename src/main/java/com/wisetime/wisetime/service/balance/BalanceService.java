package com.wisetime.wisetime.service.balance;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.balance.BalanceDTO;
import com.wisetime.wisetime.models.balance.UserBalance;
import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;
import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.balance.UserBalanceRepository;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;
import com.wisetime.wisetime.repository.punch.PunchLogRepository;
import com.wisetime.wisetime.service.dueDateBank.DueDateBankService;

@Service
public class BalanceService {

    @Autowired
    private DueDateBankService dueDateBankService;

    @Autowired
    private PunchLogRepository punchLogRepository;

    @Autowired
    private UserBalanceRepository userBalanceRepository;
    
    @Autowired
    private CertificateRepository certificateRepository;


    public void calculateAndSaveUserBalance(User user) {
        Long userId = user.getId();
        Long organizationId = user.getTeam().getOrganization().getId();

        DueDateBank currentPeriod = dueDateBankService.getCurrentDueDateBank(LocalDate.now(), organizationId);
       

        DueDateBank previousPeriod = dueDateBankService.getPreviousDueDateBank(currentPeriod.getStartDate(),
                organizationId);
        

        Duration currentPeriodBalance = calculateUserBalanceForPeriod(userId, organizationId, currentPeriod, true);

        Duration previousPeriodBalance = calculateUserBalanceForPeriod(userId, organizationId, previousPeriod, false);

        Duration totalBalance = calculateUserTotalBalance(userId, organizationId);

        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if (userBalance == null) {
            userBalance = new UserBalance();
            userBalance.setUser(user);
        }

        userBalance.setDate(LocalDate.now());
        userBalance.setCurrentPeriodBalanceInSeconds(currentPeriodBalance.getSeconds());
        userBalance.setPreviousPeriodBalanceInSeconds(previousPeriodBalance.getSeconds());
        userBalance.setTotalBalanceInSeconds(totalBalance.getSeconds());

        userBalanceRepository.save(userBalance);
    }

    public BalanceDTO getUserBalancesFromDatabase(Long userId) {
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);

        if (userBalance == null) {
            BalanceDTO balanceDTO = new BalanceDTO();
            balanceDTO.setCurrentPeriodBalance("+00:00");
            balanceDTO.setPreviousPeriodBalance("+00:00");
            balanceDTO.setTotalBalance("+00:00");
            return balanceDTO;
        }

        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCurrentPeriodBalance(formatDuration(userBalance.getCurrentPeriodBalanceInSeconds()));
        balanceDTO.setPreviousPeriodBalance(formatDuration(userBalance.getPreviousPeriodBalanceInSeconds()));
        balanceDTO.setTotalBalance(formatDuration(userBalance.getTotalBalanceInSeconds()));

        return balanceDTO;
    }



    public Duration calculateUserBalanceForPeriod(Long userId, Long organizationId, DueDateBank period, boolean isCurrentPeriod) {
    	if (period == null) {
            return Duration.ZERO;
        }
        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();

        if (isCurrentPeriod) {
            endDate = LocalDate.now();
        }

        List<LocalDate> allDates = getAllBusinessDays(userId, startDate, endDate);


        List<PunchLog> punchLogs = punchLogRepository.findByUserIdAndTimestampBetween(
                userId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        Map<LocalDate, Duration> dailyWorkHours = calculateDailyWorkHours(punchLogs, allDates);

        Map<LocalDate, Duration> dailyBalances = calculateDailyBalance(dailyWorkHours);

        Duration totalBalance = calculateTotalBalance(dailyBalances);

        return totalBalance;
    }


    public Duration calculateUserTotalBalance(Long userId, Long organizationId) {
        DueDateBank firstDueDateBank = dueDateBankService.getEarliestDueDateBank(organizationId);
        if (firstDueDateBank == null) {
            return Duration.ZERO;
        }

        LocalDate startDate = firstDueDateBank.getStartDate();
        LocalDate endDate = LocalDate.now();

        List<LocalDate> allDates = getAllBusinessDays(userId, startDate, endDate);

        List<PunchLog> punchLogs = punchLogRepository.findByUserIdAndTimestampBefore(userId, LocalDateTime.now());

        Map<LocalDate, Duration> dailyWorkHours = calculateDailyWorkHours(punchLogs, allDates);

        Map<LocalDate, Duration> dailyBalances = calculateDailyBalance(dailyWorkHours);

        Duration totalBalance = calculateTotalBalance(dailyBalances);

        return totalBalance;
    }


    public Map<LocalDate, Duration> calculateDailyWorkHours(List<PunchLog> punchLogs, List<LocalDate> allDates) {
        Map<LocalDate, List<PunchLog>> logsByDate = punchLogs.stream()
                .collect(Collectors.groupingBy(log -> log.getTimestamp().toLocalDate()));

        Map<LocalDate, Duration> dailyWorkHours = new HashMap<>();

        for (LocalDate date : allDates) {
            List<PunchLog> logs = logsByDate.getOrDefault(date, new ArrayList<>());

            if (logs.isEmpty()) {
                dailyWorkHours.put(date, Duration.ZERO);
                continue;
            }

            logs.sort(Comparator.comparing(PunchLog::getTimestamp));

            List<LocalDateTime> entries = new ArrayList<>();
            List<LocalDateTime> exits = new ArrayList<>();

            for (PunchLog log : logs) {
                if (log.getType() == PunchTypeEnum.ENTRY) {
                    entries.add(log.getTimestamp());
                } else if (log.getType() == PunchTypeEnum.EXIT) {
                    exits.add(log.getTimestamp());
                }
            }

            if (entries.size() != exits.size()) {
                if (entries.size() > exits.size()) {
                    entries = entries.subList(0, exits.size());
                } else {
                    exits = exits.subList(0, entries.size());
                }
            }

            Duration totalWorkDuration = Duration.ZERO;
            int pairs = Math.min(entries.size(), exits.size());
            for (int i = 0; i < pairs; i++) {
                LocalDateTime entryTime = entries.get(i);
                LocalDateTime exitTime = exits.get(i);
                Duration workDuration = Duration.between(entryTime, exitTime);
                totalWorkDuration = totalWorkDuration.plus(workDuration);
            }

            dailyWorkHours.put(date, totalWorkDuration);
        }

        return dailyWorkHours;
    }

    public Map<LocalDate, Duration> calculateDailyBalance(Map<LocalDate, Duration> dailyWorkHours) {
        Map<LocalDate, Duration> dailyBalances = new HashMap<>();

        Duration expectedDailyWork = Duration.ofHours(8);

        for (Map.Entry<LocalDate, Duration> entry : dailyWorkHours.entrySet()) {
            LocalDate date = entry.getKey();
            Duration actualWork = entry.getValue();
            Duration balance = actualWork.minus(expectedDailyWork);
            dailyBalances.put(date, balance);
        }

        return dailyBalances;
    }

    public Duration calculateTotalBalance(Map<LocalDate, Duration> dailyBalances) {
        Duration totalBalance = Duration.ZERO;

        for (Duration balance : dailyBalances.values()) {
            totalBalance = totalBalance.plus(balance);
        }

        return totalBalance;
    }

    private String formatDuration(Long totalSeconds) {
        long totalMinutes = totalSeconds / 60;
        String sign = totalMinutes >= 0 ? "+" : "-";
        totalMinutes = Math.abs(totalMinutes);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%s%02d:%02d", sign, hours, minutes);
    }
    
    public List<LocalDate> getAllBusinessDays(Long userId, LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidays = getStaticHolidays(startDate.getYear(), endDate.getYear());
        Set<LocalDate> certificateDays = getApprovedCertificateDates(userId);

        return startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> isBusinessDay(date, holidays) && !certificateDays.contains(date))
                .collect(Collectors.toList());
    }

    private boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }
        if (holidays.contains(date)) {
            return false;
        }
        return true;
    }

    private Set<LocalDate> getStaticHolidays(int startYear, int endYear) {
        Set<LocalDate> holidays = new HashSet<>();
        
        for (int year = startYear; year <= endYear; year++) {
            holidays.add(LocalDate.of(year, Month.JANUARY, 1));   
            holidays.add(LocalDate.of(year, Month.APRIL, 21));    
            holidays.add(LocalDate.of(year, Month.MAY, 1));      
            holidays.add(LocalDate.of(year, Month.SEPTEMBER, 7)); 
            holidays.add(LocalDate.of(year, Month.OCTOBER, 12));  
            holidays.add(LocalDate.of(year, Month.NOVEMBER, 2));  
            holidays.add(LocalDate.of(year, Month.NOVEMBER, 15)); 
            holidays.add(LocalDate.of(year, Month.DECEMBER, 25));
        }

        return holidays;
    }
    
    private Set<LocalDate> getApprovedCertificateDates(Long userId) {
        List<Certificate> approvedCertificates = certificateRepository.findByUserIdAndStatus(
            userId, CertificateStatusEnum.APROVADO);

        Set<LocalDate> certificateDays = new HashSet<>();
        for (Certificate certificate : approvedCertificates) {
            LocalDate startDate = certificate.getStartDate();
            LocalDate endDate = certificate.getEndDate();
            certificateDays.addAll(startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toSet()));
        }
        return certificateDays;
    }
    
}

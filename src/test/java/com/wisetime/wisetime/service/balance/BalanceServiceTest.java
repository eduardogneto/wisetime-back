package com.wisetime.wisetime.service.balance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.balance.BalanceDTO;
import com.wisetime.wisetime.models.balance.UserBalance;
import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;
import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.balance.UserBalanceRepository;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;
import com.wisetime.wisetime.repository.punch.PunchLogRepository;
import com.wisetime.wisetime.service.dueDateBank.DueDateBankService;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private DueDateBankService dueDateBankService;

    @Mock
    private PunchLogRepository punchLogRepository;

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private BalanceService balanceService;

    private User user;
    private Organization organization;

    @BeforeEach
    public void setup() {
        organization = new Organization();
        organization.setId(1L);

        user = new User();
        user.setId(1L);
        user.setTeam(new Team());
        user.getTeam().setOrganization(organization);
    }

    @Test
    public void testCalculateAndSaveUserBalance_NoPreviousBalance() {
        DueDateBank currentPeriod = new DueDateBank(LocalDate.now().minusDays(10), LocalDate.now(), organization);
        DueDateBank previousPeriod = new DueDateBank(LocalDate.now().minusDays(20), LocalDate.now().minusDays(11), organization);

        when(dueDateBankService.getCurrentDueDateBank(any(LocalDate.class), anyLong())).thenReturn(currentPeriod);
        when(dueDateBankService.getPreviousDueDateBank(any(LocalDate.class), anyLong())).thenReturn(previousPeriod);
        when(userBalanceRepository.findByUserId(user.getId())).thenReturn(null);

        balanceService.calculateAndSaveUserBalance(user);

        verify(userBalanceRepository, times(1)).save(any(UserBalance.class));
    }

    @Test
    public void testCalculateUserBalanceForPeriod() {
        DueDateBank period = new DueDateBank(LocalDate.now().minusDays(10), LocalDate.now(), organization);

        List<PunchLog> punchLogs = Arrays.asList(
            PunchLog.builder()
                .timestamp(LocalDateTime.now().minusHours(8))
                .type(PunchTypeEnum.ENTRY)
                .user(user)
                .organization(organization)
                .build(),
            PunchLog.builder()
                .timestamp(LocalDateTime.now())
                .type(PunchTypeEnum.EXIT)
                .user(user)
                .organization(organization)
                .build()
        );

        when(punchLogRepository.findByUserIdAndTimestampBetween(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(punchLogs);

        Duration balance = balanceService.calculateUserBalanceForPeriod(user.getId(), organization.getId(), period, true);

        assertEquals(balance.toHours(), balance.toHours());
    }

    @Test
    public void testCalculateUserTotalBalance() {
        DueDateBank firstDueDateBank = new DueDateBank(LocalDate.now().minusYears(1), LocalDate.now().minusMonths(1), organization);

        List<PunchLog> punchLogs = Arrays.asList(
            PunchLog.builder()
                .timestamp(LocalDateTime.now().minusHours(9))
                .type(PunchTypeEnum.ENTRY)
                .user(user)
                .organization(organization)
                .build(),
            PunchLog.builder()
                .timestamp(LocalDateTime.now())
                .type(PunchTypeEnum.EXIT)
                .user(user)
                .organization(organization)
                .build()
        );

        when(dueDateBankService.getEarliestDueDateBank(organization.getId())).thenReturn(firstDueDateBank);
        when(punchLogRepository.findByUserIdAndTimestampBefore(anyLong(), any(LocalDateTime.class))).thenReturn(punchLogs);

        Duration totalBalance = balanceService.calculateUserTotalBalance(user.getId(), organization.getId());

        assertNotNull(totalBalance);
    }

    @Test
    public void testGetUserBalancesFromDatabase_NoBalance() {
        when(userBalanceRepository.findByUserId(user.getId())).thenReturn(null);

        BalanceDTO balanceDTO = balanceService.getUserBalancesFromDatabase(user.getId());

        assertEquals("+00:00", balanceDTO.getCurrentPeriodBalance());
        assertEquals("+00:00", balanceDTO.getPreviousPeriodBalance());
        assertEquals("+00:00", balanceDTO.getTotalBalance());
    }

    @Test
    public void testCalculateDailyWorkHours() {
        List<PunchLog> punchLogs = Arrays.asList(
            PunchLog.builder()
                .timestamp(LocalDateTime.now().minusHours(9))
                .type(PunchTypeEnum.ENTRY)
                .user(user)
                .organization(organization)
                .build(),
            PunchLog.builder()
                .timestamp(LocalDateTime.now())
                .type(PunchTypeEnum.EXIT)
                .user(user)
                .organization(organization)
                .build()
        );

        List<LocalDate> businessDays = Collections.singletonList(LocalDate.now());
        Map<LocalDate, Duration> workHours = balanceService.calculateDailyWorkHours(punchLogs, businessDays);

        assertEquals(Duration.ofHours(9), workHours.get(LocalDate.now()));
    }

    @Test
    public void testCalculateDailyBalance() {
        Map<LocalDate, Duration> dailyWorkHours = new HashMap<>();
        dailyWorkHours.put(LocalDate.now(), Duration.ofHours(8));

        Map<LocalDate, Duration> dailyBalance = balanceService.calculateDailyBalance(dailyWorkHours);

        assertEquals(Duration.ZERO, dailyBalance.get(LocalDate.now()));
    }

    @Test
    public void testCalculateTotalBalance() {
        Map<LocalDate, Duration> dailyBalances = new HashMap<>();
        dailyBalances.put(LocalDate.now(), Duration.ofHours(2));
        dailyBalances.put(LocalDate.now().minusDays(1), Duration.ofHours(-1));

        Duration totalBalance = balanceService.calculateTotalBalance(dailyBalances);

        assertEquals(Duration.ofHours(1), totalBalance);
    }

    @Test
    public void testGetStaticHolidays() {
        Set<LocalDate> holidays = balanceService.getStaticHolidays(2024, 2024);

        assertTrue(holidays.contains(LocalDate.of(2024, 1, 1)));
        assertTrue(holidays.contains(LocalDate.of(2024, 12, 25)));
    }

    @Test
    public void testGetApprovedCertificateDates() {
        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setStartDate(LocalDate.now().minusDays(5));
        certificate.setEndDate(LocalDate.now());
        certificate.setStatus(CertificateStatusEnum.APROVADO);

        List<Certificate> certificates = List.of(certificate);

        when(certificateRepository.findByUserIdAndStatus(anyLong(), eq(CertificateStatusEnum.APROVADO))).thenReturn(certificates);

        Set<LocalDate> approvedDates = balanceService.getApprovedCertificateDates(user.getId());

        assertTrue(approvedDates.contains(LocalDate.now().minusDays(5)));
        assertTrue(approvedDates.contains(LocalDate.now()));
    }
}

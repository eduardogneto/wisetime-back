package com.wisetime.wisetime.service.punchLog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.punch.PunchLogDTO;
import com.wisetime.wisetime.DTO.punch.PunchSummaryDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.punch.PunchLogRepository;
import com.wisetime.wisetime.service.balance.BalanceService;
import com.wisetime.wisetime.service.punch.PunchLogService;
import com.wisetime.wisetime.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class PunchLogServiceTest {

    @Mock
    private PunchLogRepository punchLogRepository;

    @Mock
    private UserService userService;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private PunchLogService punchLogService;

    private User user;
    private PunchLog punchLog;
    private PunchLogDTO punchLogDTO;
    private Organization organization;
    private Team team;

    @BeforeEach
    public void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization Name");

        team = new Team();
        team.setId(1L);
        team.setName("Team Name");
        team.setOrganization(organization);

        user = new User();
        user.setId(1L);
        user.setName("User Name");
        user.setTeam(team);

        punchLog = new PunchLog();
        punchLog.setId(1L);
        punchLog.setUser(user);
        punchLog.setTimestamp(LocalDateTime.now());
        punchLog.setType(PunchTypeEnum.ENTRY);
        punchLog.setLocation("Office");
        punchLog.setOrganization(organization);

        punchLogDTO = new PunchLogDTO();
        punchLogDTO.setUserId(user.getId());
        punchLogDTO.setTimestamp(LocalDateTime.now());
        punchLogDTO.setLocation("Office");
    }

    @Test
    public void testSave_PunchLogWithExitType_CallsBalanceService() {
        punchLog.setType(PunchTypeEnum.EXIT);

        when(punchLogRepository.save(punchLog)).thenReturn(punchLog);

        PunchLog result = punchLogService.save(punchLog);

        assertNotNull(result);
        verify(punchLogRepository).save(punchLog);
        verify(balanceService).calculateAndSaveUserBalance(user);
    }

    @Test
    public void testSave_PunchLogWithEntryType_DoesNotCallBalanceService() {
        punchLog.setType(PunchTypeEnum.ENTRY);

        when(punchLogRepository.save(punchLog)).thenReturn(punchLog);

        PunchLog result = punchLogService.save(punchLog);

        assertNotNull(result);
        verify(punchLogRepository).save(punchLog);
        verify(balanceService, never()).calculateAndSaveUserBalance(any(User.class));
    }

    @Test
    public void testFindById_PunchLogExists() {
        when(punchLogRepository.findById(1L)).thenReturn(Optional.of(punchLog));

        PunchLog result = punchLogService.findById(1L);

        assertNotNull(result);
        assertEquals(punchLog, result);
        verify(punchLogRepository).findById(1L);
    }

    @Test
    public void testFindById_PunchLogDoesNotExist() {
        when(punchLogRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            punchLogService.findById(1L);
        });

        assertEquals("Batida de ponto não encontrada", exception.getMessage());
        verify(punchLogRepository).findById(1L);
    }

    @Test
    public void testGetPunchLogsByUserAndDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserIdAndTimestampBetween(user.getId(), start, end)).thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.getPunchLogsByUserAndDateRange(user.getId(), start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserIdAndTimestampBetween(user.getId(), start, end);
    }

    @Test
    public void testGetPunchLogsByUser() {
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserId(user.getId())).thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.getPunchLogsByUser(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserId(user.getId());
    }

    @Test
    public void testGetLastPunchLog_Found() {
        when(punchLogRepository.findLastPunchLogByUserId(user.getId())).thenReturn(Arrays.asList(punchLog));

        Optional<PunchLog> result = punchLogService.getLastPunchLog(user.getId());

        assertTrue(result.isPresent());
        assertEquals(punchLog, result.get());
        verify(punchLogRepository).findLastPunchLogByUserId(user.getId());
    }

    @Test
    public void testGetLastPunchLog_NotFound() {
        when(punchLogRepository.findLastPunchLogByUserId(user.getId())).thenReturn(Collections.emptyList());

        Optional<PunchLog> result = punchLogService.getLastPunchLog(user.getId());

        assertFalse(result.isPresent());
        verify(punchLogRepository).findLastPunchLogByUserId(user.getId());
    }

    @Test
    public void testAdjustSubsequentPunchLogs() {
        PunchLog subsequentLog1 = new PunchLog();
        subsequentLog1.setId(2L);
        subsequentLog1.setType(PunchTypeEnum.EXIT);
        subsequentLog1.setTimestamp(LocalDateTime.now().plusHours(1));
        subsequentLog1.setUser(user);

        PunchLog subsequentLog2 = new PunchLog();
        subsequentLog2.setId(3L);
        subsequentLog2.setType(PunchTypeEnum.ENTRY);
        subsequentLog2.setTimestamp(LocalDateTime.now().plusHours(2));
        subsequentLog2.setUser(user);

        List<PunchLog> subsequentLogs = Arrays.asList(subsequentLog1, subsequentLog2);

        when(punchLogRepository.findSubsequentPunchLogs(user.getId(), punchLog.getTimestamp())).thenReturn(subsequentLogs);

        punchLogService.adjustSubsequentPunchLogs(user.getId(), punchLog.getTimestamp(), PunchTypeEnum.ENTRY);

        assertEquals(PunchTypeEnum.EXIT, subsequentLog1.getType());
        assertEquals(PunchTypeEnum.ENTRY, subsequentLog2.getType());

        verify(punchLogRepository).findSubsequentPunchLogs(user.getId(), punchLog.getTimestamp());
        verify(punchLogRepository, times(2)).save(any(PunchLog.class));
    }

    @Test
    public void testFindByUserId() {
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserId(user.getId())).thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.findByUserId(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserId(user.getId());
    }

    @Test
    public void testLogPunch_FirstPunch_EntryType() {
        when(userService.findEntityById(user.getId())).thenReturn(Optional.of(user));
        when(punchLogRepository.findLastPunchLogByUserId(user.getId())).thenReturn(Collections.emptyList());
        when(punchLogRepository.save(any(PunchLog.class))).thenReturn(punchLog);

        PunchLog result = punchLogService.logPunch(punchLogDTO);

        assertNotNull(result);
        assertEquals(PunchTypeEnum.ENTRY, result.getType());
        verify(punchLogRepository).save(any(PunchLog.class));
    }

    @Test
    public void testLogPunch_UserNotFound() {
        when(userService.findEntityById(user.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            punchLogService.logPunch(punchLogDTO);
        });

        assertEquals("Usuário não encontrado para o ID: " + user.getId(), exception.getMessage());
        verify(userService).findEntityById(user.getId());
        verify(punchLogRepository, never()).save(any(PunchLog.class));
    }

    @Test
    public void testGetPunchHistoryForDay() {
        LocalDate date = LocalDate.now();
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserIdAndTimestampBetween(
                eq(user.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.getPunchHistoryForDay(user.getId(), date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserIdAndTimestampBetween(
                eq(user.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    public void testGetPunchHistorySummary() {
        PunchLog punchLog1 = new PunchLog();
        punchLog1.setTimestamp(LocalDateTime.now());
        punchLog1.setType(PunchTypeEnum.ENTRY);

        PunchLog punchLog2 = new PunchLog();
        punchLog2.setTimestamp(LocalDateTime.now());
        punchLog2.setType(PunchTypeEnum.EXIT);

        when(punchLogRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(punchLog1, punchLog2));

        List<PunchSummaryDTO> result = punchLogService.getPunchHistorySummary(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        PunchSummaryDTO summary = result.get(0);
        assertEquals("Completo", summary.getStatus());
        assertEquals(1, summary.getEntryCount());
        assertEquals(1, summary.getExitCount());
        verify(punchLogRepository).findByUserId(user.getId());
    }

    @Test
    public void testGetPunchLogsByUserAndPeriod() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserIdAndTimestampBetween(
                eq(user.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.getPunchLogsByUserAndPeriod(user.getId(), startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserIdAndTimestampBetween(
                eq(user.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    public void testGetPunchLogsByUserAndOrganization() {
        List<PunchLog> punchLogs = Arrays.asList(punchLog);

        when(punchLogRepository.findByUserIdAndOrganizationId(user.getId(), organization.getId()))
                .thenReturn(punchLogs);

        List<PunchLog> result = punchLogService.getPunchLogsByUserAndOrganization(user.getId(), organization.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(punchLogRepository).findByUserIdAndOrganizationId(user.getId(), organization.getId());
    }
}

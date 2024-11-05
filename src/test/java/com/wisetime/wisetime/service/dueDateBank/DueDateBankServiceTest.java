package com.wisetime.wisetime.service.dueDateBank;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.dueDateBank.DueDateBankRepository;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;

@ExtendWith(MockitoExtension.class)
public class DueDateBankServiceTest {

    @Mock
    private DueDateBankRepository dueDateBankRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private DueDateBankService dueDateBankService;

    private Organization organization;
    private DueDateBank dueDateBank;

    @BeforeEach
    public void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization Name");

        dueDateBank = new DueDateBank();
        dueDateBank.setId(1L);
        dueDateBank.setStartDate(LocalDate.now().minusMonths(1));
        dueDateBank.setEndDate(LocalDate.now().plusMonths(1));
        dueDateBank.setOrganization(organization);
    }

    @Test
    public void testFindByOrganizationId_DueDateBanksExist() {
        when(dueDateBankRepository.findByOrganizationId(organization.getId()))
                .thenReturn(Collections.singletonList(dueDateBank));

        List<DueDateBank> result = dueDateBankService.findByOrganizationId(organization.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dueDateBank, result.get(0));
        verify(dueDateBankRepository).findByOrganizationId(organization.getId());
    }

    @Test
    public void testFindByOrganizationId_NoDueDateBanksExist() {
        when(dueDateBankRepository.findByOrganizationId(organization.getId()))
                .thenReturn(Collections.emptyList());

        List<DueDateBank> result = dueDateBankService.findByOrganizationId(organization.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dueDateBankRepository).findByOrganizationId(organization.getId());
    }

    @Test
    public void testFindAllCurrentDueDateBanks_CurrentDueDateBanksExist() {
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));

        List<DueDateBank> result = dueDateBankService.findAllCurrentDueDateBanks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dueDateBank, result.get(0));
        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
    }

    @Test
    public void testFindAllCurrentDueDateBanks_NoCurrentDueDateBanksExist() {
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        List<DueDateBank> result = dueDateBankService.findAllCurrentDueDateBanks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
    }

    @Test
    public void testExistsByStartDateAndOrganization_DueDateBankExists() {
        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(true);

        boolean exists = dueDateBankService.existsByStartDateAndOrganization(LocalDate.now(), organization);

        assertTrue(exists);
        verify(dueDateBankRepository).existsByStartDateAndOrganization(any(LocalDate.class), eq(organization));
    }

    @Test
    public void testExistsByStartDateAndOrganization_DueDateBankDoesNotExist() {
        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(false);

        boolean exists = dueDateBankService.existsByStartDateAndOrganization(LocalDate.now(), organization);

        assertFalse(exists);
        verify(dueDateBankRepository).existsByStartDateAndOrganization(any(LocalDate.class), eq(organization));
    }

    @Test
    public void testSaveDueDateBank() {
        when(dueDateBankRepository.save(any(DueDateBank.class))).thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.saveDueDateBank(dueDateBank);

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).save(dueDateBank);
    }

    @Test
    public void testGetCurrentDueDateBank_CurrentDueDateBankExists() {
        LocalDate date = LocalDate.now();
        when(dueDateBankRepository.findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                eq(organization.getId()), eq(date), eq(date)))
                .thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.getCurrentDueDateBank(date, organization.getId());

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                eq(organization.getId()), eq(date), eq(date));
    }

    @Test
    public void testGetCurrentDueDateBank_CurrentDueDateBankDoesNotExist() {
        LocalDate date = LocalDate.now();
        when(dueDateBankRepository.findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                eq(organization.getId()), eq(date), eq(date)))
                .thenReturn(null);

        DueDateBank result = dueDateBankService.getCurrentDueDateBank(date, organization.getId());

        assertNull(result);
        verify(dueDateBankRepository).findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                eq(organization.getId()), eq(date), eq(date));
    }

    @Test
    public void testGetPreviousDueDateBank_PreviousDueDateBankExists() {
        LocalDate date = LocalDate.now();
        when(dueDateBankRepository.findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
                eq(organization.getId()), eq(date)))
                .thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.getPreviousDueDateBank(date, organization.getId());

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
                eq(organization.getId()), eq(date));
    }

    @Test
    public void testGetPreviousDueDateBank_PreviousDueDateBankDoesNotExist() {
        LocalDate date = LocalDate.now();
        when(dueDateBankRepository.findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
                eq(organization.getId()), eq(date)))
                .thenReturn(null);

        DueDateBank result = dueDateBankService.getPreviousDueDateBank(date, organization.getId());

        assertNull(result);
        verify(dueDateBankRepository).findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
                eq(organization.getId()), eq(date));
    }

    @Test
    public void testGetEarliestDueDateBank_EarliestDueDateBankExists() {
        when(dueDateBankRepository.findTopByOrganizationIdOrderByStartDateAsc(eq(organization.getId())))
                .thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.getEarliestDueDateBank(organization.getId());

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).findTopByOrganizationIdOrderByStartDateAsc(organization.getId());
    }

    @Test
    public void testGetEarliestDueDateBank_EarliestDueDateBankDoesNotExist() {
        when(dueDateBankRepository.findTopByOrganizationIdOrderByStartDateAsc(eq(organization.getId())))
                .thenReturn(null);

        DueDateBank result = dueDateBankService.getEarliestDueDateBank(organization.getId());

        assertNull(result);
        verify(dueDateBankRepository).findTopByOrganizationIdOrderByStartDateAsc(organization.getId());
    }

    @Test
    public void testCreateNextDueDateBanks_NoCurrentDueDateBanks() {
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        when(organizationRepository.findAll())
                .thenReturn(Collections.singletonList(organization));

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
        verify(organizationRepository).findAll();
        verify(dueDateBankRepository, times(1)).save(any(DueDateBank.class));
    }

    @Test
    public void testCreateNextDueDateBanks_CurrentDueDateBanksExistAndWithinFiveDays() {
        dueDateBank.setEndDate(LocalDate.now().plusDays(3));

        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));

        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(false);

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
        verify(dueDateBankRepository).existsByStartDateAndOrganization(any(LocalDate.class), eq(organization));
        verify(dueDateBankRepository).save(any(DueDateBank.class));
    }

    @Test
    public void testCreateNextDueDateBanks_CurrentDueDateBanksExistButNotWithinFiveDays() {
        dueDateBank.setEndDate(LocalDate.now().plusDays(10));

        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
        verify(dueDateBankRepository, never()).existsByStartDateAndOrganization(any(LocalDate.class), any(Organization.class));
        verify(dueDateBankRepository, never()).save(any(DueDateBank.class));
    }

    @Test
    public void testCreateNextDueDateBanks_CurrentDueDateBanksExistWithinFiveDaysButNextPeriodExists() {
        dueDateBank.setEndDate(LocalDate.now().plusDays(3));

        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));

        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(true);

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
        verify(dueDateBankRepository).existsByStartDateAndOrganization(any(LocalDate.class), eq(organization));
        verify(dueDateBankRepository, never()).save(any(DueDateBank.class));
    }
}

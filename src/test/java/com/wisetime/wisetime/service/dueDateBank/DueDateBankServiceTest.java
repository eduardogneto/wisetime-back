package com.wisetime.wisetime.service.dueDateBank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
        organization = Organization.builder()
                .id(1L)
                .name("Desenvolvimento")
                .build();

        dueDateBank = DueDateBank.builder()
                .id(1L)
                .startDate(LocalDate.now().minusMonths(1))
                .endDate(LocalDate.now().plusMonths(1))
                .organization(organization)
                .build();
    }

    @Test
    public void testFindByOrganizationId_Success() {
        when(dueDateBankRepository.findByOrganizationId(organization.getId()))
                .thenReturn(Collections.singletonList(dueDateBank));

        List<DueDateBank> result = dueDateBankService.findByOrganizationId(organization.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dueDateBank, result.get(0));
        verify(dueDateBankRepository).findByOrganizationId(organization.getId());
    }

    @Test
    public void testFindAllCurrentDueDateBanks_Success() {
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));

        List<DueDateBank> result = dueDateBankService.findAllCurrentDueDateBanks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dueDateBank, result.get(0));
        verify(dueDateBankRepository).findByEndDateAfter(any(LocalDate.class));
    }

    @Test
    public void testExistsByStartDateAndOrganization_Exists() {
        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(true);

        boolean result = dueDateBankService.existsByStartDateAndOrganization(LocalDate.now(), organization);

        assertTrue(result);
        verify(dueDateBankRepository).existsByStartDateAndOrganization(any(LocalDate.class), eq(organization));
    }

    @Test
    public void testSaveDueDateBank_Success() {
        when(dueDateBankRepository.save(any(DueDateBank.class))).thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.saveDueDateBank(dueDateBank);

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).save(dueDateBank);
    }

    @Test
    public void testGetCurrentDueDateBank_Success() {
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
    public void testGetPreviousDueDateBank_Success() {
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
    public void testGetEarliestDueDateBank_Success() {
        when(dueDateBankRepository.findTopByOrganizationIdOrderByStartDateAsc(eq(organization.getId())))
                .thenReturn(dueDateBank);

        DueDateBank result = dueDateBankService.getEarliestDueDateBank(organization.getId());

        assertNotNull(result);
        assertEquals(dueDateBank, result);
        verify(dueDateBankRepository).findTopByOrganizationIdOrderByStartDateAsc(organization.getId());
    }

    @Test
    public void testCreateNextDueDateBanks_NoCurrentBanks() {
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(organizationRepository.findAll()).thenReturn(Collections.singletonList(organization));

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository, times(1)).save(any(DueDateBank.class));
    }

    @Test
    public void testCreateNextDueDateBanks_WithCurrentBanks() {
        dueDateBank.setEndDate(LocalDate.now().plusDays(4));
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));
        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(false);

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository, times(1)).save(any(DueDateBank.class));
    }

    @Test
    public void testCreateNextDueDateBanks_ExistingNextPeriod() {
        dueDateBank.setEndDate(LocalDate.now().plusDays(4));
        when(dueDateBankRepository.findByEndDateAfter(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(dueDateBank));
        when(dueDateBankRepository.existsByStartDateAndOrganization(any(LocalDate.class), eq(organization)))
                .thenReturn(true);

        dueDateBankService.createNextDueDateBanks();

        verify(dueDateBankRepository, times(0)).save(any(DueDateBank.class));
    }
}

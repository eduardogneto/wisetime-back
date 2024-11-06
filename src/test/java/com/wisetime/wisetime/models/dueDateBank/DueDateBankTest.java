package com.wisetime.wisetime.models.dueDateBank;

import com.wisetime.wisetime.models.organization.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DueDateBankTest {

    private Organization organization;
    private DueDateBank dueDateBank;

    @BeforeEach
    public void setup() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");
    }

    @Test
    public void testGetTagProximo() {
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(10);
        dueDateBank = new DueDateBank(startDate, endDate, organization);

        assertEquals(TagDueDateBankEnum.PROXIMO, dueDateBank.getTag());
    }

    @Test
    public void testGetTagAndamento() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);
        dueDateBank = new DueDateBank(startDate, endDate, organization);

        assertEquals(TagDueDateBankEnum.ANDAMENTO, dueDateBank.getTag());
    }

    @Test
    public void testGetTagCompleto() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(5);
        dueDateBank = new DueDateBank(startDate, endDate, organization);

        assertEquals(TagDueDateBankEnum.COMPLETO, dueDateBank.getTag());
    }

    @Test
    public void testConstructorWithArguments() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        dueDateBank = new DueDateBank(startDate, endDate, organization);

        assertEquals(startDate, dueDateBank.getStartDate());
        assertEquals(endDate, dueDateBank.getEndDate());
        assertEquals(organization, dueDateBank.getOrganization());
    }

    @Test
    public void testSettersAndGetters() {
        dueDateBank = new DueDateBank();
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        dueDateBank.setId(1L);
        dueDateBank.setStartDate(startDate);
        dueDateBank.setEndDate(endDate);
        dueDateBank.setOrganization(organization);

        assertEquals(1L, dueDateBank.getId());
        assertEquals(startDate, dueDateBank.getStartDate());
        assertEquals(endDate, dueDateBank.getEndDate());
        assertEquals(organization, dueDateBank.getOrganization());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDate startDate = LocalDate.of(2023, 5, 1);
        LocalDate endDate = LocalDate.of(2023, 6, 1);
        DueDateBank dueDateBank1 = new DueDateBank(startDate, endDate, organization);
        dueDateBank1.setId(1L);

        DueDateBank dueDateBank2 = new DueDateBank(startDate, endDate, organization);
        dueDateBank2.setId(1L);

        DueDateBank dueDateBank3 = new DueDateBank(startDate, endDate, organization);
        dueDateBank3.setId(2L);

        assertEquals(dueDateBank1, dueDateBank2);
        assertEquals(dueDateBank1.hashCode(), dueDateBank2.hashCode());
        assertNotEquals(dueDateBank1, dueDateBank3);
        assertNotEquals(dueDateBank1.hashCode(), dueDateBank3.hashCode());
    }

    @Test
    public void testToString() {
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);
        dueDateBank = new DueDateBank(startDate, endDate, organization);
        dueDateBank.setId(1L);

        String result = dueDateBank.toString();
        assertNotNull(result);
        assertTrue(result.contains("DueDateBank"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("startDate=2023-07-01"));
        assertTrue(result.contains("endDate=2023-07-31"));
        assertTrue(result.contains("organization=Organization"));
    }
}

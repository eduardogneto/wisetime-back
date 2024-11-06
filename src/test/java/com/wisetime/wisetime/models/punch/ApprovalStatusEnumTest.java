package com.wisetime.wisetime.models.punch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ApprovalStatusEnumTest {

    @Test
    public void testEnumValuesCount() {
        ApprovalStatusEnum[] statuses = ApprovalStatusEnum.values();
        assertEquals(3, statuses.length, "O enum deve conter exatamente 3 status.");
    }

    @Test
    public void testEnumValuesContent() {
        ApprovalStatusEnum[] statuses = ApprovalStatusEnum.values();
        assertArrayEquals(
                new ApprovalStatusEnum[]{ApprovalStatusEnum.PENDING, ApprovalStatusEnum.APPROVED, ApprovalStatusEnum.REJECTED},
                statuses,
                "Os valores do enum não correspondem aos esperados."
        );
    }

    @Test
    public void testValueOf() {
        assertEquals(ApprovalStatusEnum.PENDING, ApprovalStatusEnum.valueOf("PENDING"));
        assertEquals(ApprovalStatusEnum.APPROVED, ApprovalStatusEnum.valueOf("APPROVED"));
        assertEquals(ApprovalStatusEnum.REJECTED, ApprovalStatusEnum.valueOf("REJECTED"));
    }

    @Test
    public void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            ApprovalStatusEnum.valueOf("INVALID_STATUS");
        }, "Deve lançar IllegalArgumentException para um valor inválido.");
    }
}

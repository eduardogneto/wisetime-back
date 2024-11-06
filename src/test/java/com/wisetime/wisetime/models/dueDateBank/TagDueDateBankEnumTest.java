package com.wisetime.wisetime.models.dueDateBank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagDueDateBankEnumTest {

    @Test
    public void testEnumValues() {
        assertEquals(3, TagDueDateBankEnum.values().length);
        assertEquals(TagDueDateBankEnum.COMPLETO, TagDueDateBankEnum.valueOf("COMPLETO"));
        assertEquals(TagDueDateBankEnum.ANDAMENTO, TagDueDateBankEnum.valueOf("ANDAMENTO"));
        assertEquals(TagDueDateBankEnum.PROXIMO, TagDueDateBankEnum.valueOf("PROXIMO"));
    }

    @Test
    public void testEnumToString() {
        assertEquals("COMPLETO", TagDueDateBankEnum.COMPLETO.toString());
        assertEquals("ANDAMENTO", TagDueDateBankEnum.ANDAMENTO.toString());
        assertEquals("PROXIMO", TagDueDateBankEnum.PROXIMO.toString());
    }
}

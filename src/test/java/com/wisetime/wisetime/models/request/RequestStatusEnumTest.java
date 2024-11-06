package com.wisetime.wisetime.models.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestStatusEnumTest {

    @Test
    public void testEnumValues() {
        assertEquals("PENDENTE", RequestStatusEnum.PENDENTE.name());
        assertEquals("APROVADO", RequestStatusEnum.APROVADO.name());
        assertEquals("REPROVADO", RequestStatusEnum.REPROVADO.name());
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(RequestStatusEnum.PENDENTE, RequestStatusEnum.valueOf("PENDENTE"));
        assertEquals(RequestStatusEnum.APROVADO, RequestStatusEnum.valueOf("APROVADO"));
        assertEquals(RequestStatusEnum.REPROVADO, RequestStatusEnum.valueOf("REPROVADO"));
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, RequestStatusEnum.PENDENTE.ordinal());
        assertEquals(1, RequestStatusEnum.APROVADO.ordinal());
        assertEquals(2, RequestStatusEnum.REPROVADO.ordinal());
    }
}

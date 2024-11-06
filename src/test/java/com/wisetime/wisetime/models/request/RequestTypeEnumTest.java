package com.wisetime.wisetime.models.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTypeEnumTest {

    @Test
    public void testEnumValues() {
        assertEquals("ADICAO_DE_PONTO", RequestTypeEnum.ADICAO_DE_PONTO.name());
        assertEquals("ABONO", RequestTypeEnum.ABONO.name());
        assertEquals("ATESTADO", RequestTypeEnum.ATESTADO.name());
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(RequestTypeEnum.ADICAO_DE_PONTO, RequestTypeEnum.valueOf("ADICAO_DE_PONTO"));
        assertEquals(RequestTypeEnum.ABONO, RequestTypeEnum.valueOf("ABONO"));
        assertEquals(RequestTypeEnum.ATESTADO, RequestTypeEnum.valueOf("ATESTADO"));
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, RequestTypeEnum.ADICAO_DE_PONTO.ordinal());
        assertEquals(1, RequestTypeEnum.ABONO.ordinal());
        assertEquals(2, RequestTypeEnum.ATESTADO.ordinal());
    }
}

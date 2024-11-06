package com.wisetime.wisetime.models.punch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PunchTypeEnumTest {

    @Test
    public void testEnumValues() {
        assertEquals("ENTRY", PunchTypeEnum.ENTRY.name());
        assertEquals("EXIT", PunchTypeEnum.EXIT.name());
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(PunchTypeEnum.ENTRY, PunchTypeEnum.valueOf("ENTRY"));
        assertEquals(PunchTypeEnum.EXIT, PunchTypeEnum.valueOf("EXIT"));
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, PunchTypeEnum.ENTRY.ordinal());
        assertEquals(1, PunchTypeEnum.EXIT.ordinal());
    }
}

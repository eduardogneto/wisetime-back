package com.wisetime.wisetime.models.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagUserEnumTest {

    @Test
    public void testEnumValues() {
        assertEquals("ADMINISTRADOR", TagUserEnum.ADMINISTRADOR.name());
        assertEquals("COORDENADOR", TagUserEnum.COORDENADOR.name());
        assertEquals("FUNCIONARIO", TagUserEnum.FUNCIONARIO.name());
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(TagUserEnum.ADMINISTRADOR, TagUserEnum.valueOf("ADMINISTRADOR"));
        assertEquals(TagUserEnum.COORDENADOR, TagUserEnum.valueOf("COORDENADOR"));
        assertEquals(TagUserEnum.FUNCIONARIO, TagUserEnum.valueOf("FUNCIONARIO"));
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, TagUserEnum.ADMINISTRADOR.ordinal());
        assertEquals(1, TagUserEnum.COORDENADOR.ordinal());
        assertEquals(2, TagUserEnum.FUNCIONARIO.ordinal());
    }
}

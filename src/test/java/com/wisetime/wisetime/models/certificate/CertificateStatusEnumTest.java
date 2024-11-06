package com.wisetime.wisetime.models.certificate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CertificateStatusEnumTest {

    @Test
    public void testEnumValues() {
        CertificateStatusEnum[] values = CertificateStatusEnum.values();
        assertEquals(3, values.length);
        assertArrayEquals(
            new CertificateStatusEnum[] {
                CertificateStatusEnum.PENDENTE,
                CertificateStatusEnum.APROVADO,
                CertificateStatusEnum.REPROVADO
            },
            values
        );
    }

    @Test
    public void testValueOf() {
        assertEquals(CertificateStatusEnum.PENDENTE, CertificateStatusEnum.valueOf("PENDENTE"));
        assertEquals(CertificateStatusEnum.APROVADO, CertificateStatusEnum.valueOf("APROVADO"));
        assertEquals(CertificateStatusEnum.REPROVADO, CertificateStatusEnum.valueOf("REPROVADO"));
    }

    @Test
    public void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            CertificateStatusEnum.valueOf("INVALID_STATUS");
        });
    }
}

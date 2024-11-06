package com.wisetime.wisetime.models.certificate;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CertificateTest {

    @Test
    public void testNoArgsConstructor() {
        Certificate certificate = new Certificate();
        assertNotNull(certificate);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Request request = new Request();
        request.setId(2L);

        byte[] imageData = new byte[] {1, 2, 3};

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        Certificate certificate = new Certificate(
            1L,
            user,
            startDate,
            endDate,
            "Justification",
            imageData,
            CertificateStatusEnum.PENDENTE,
            request
        );

        assertEquals(1L, certificate.getId());
        assertEquals(user, certificate.getUser());
        assertEquals(startDate, certificate.getStartDate());
        assertEquals(endDate, certificate.getEndDate());
        assertEquals("Justification", certificate.getJustification());
        assertArrayEquals(imageData, certificate.getImageData());
        assertEquals(CertificateStatusEnum.PENDENTE, certificate.getStatus());
        assertEquals(request, certificate.getRequest());
    }

    @Test
    public void testBuilder() {
        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(2L);

        byte[] imageData = new byte[] {1, 2, 3};

        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 28);

        Certificate certificate = Certificate.builder()
            .id(1L)
            .user(user)
            .startDate(startDate)
            .endDate(endDate)
            .justification("Justification")
            .imageData(imageData)
            .status(CertificateStatusEnum.APROVADO)
            .request(request)
            .build();

        assertEquals(1L, certificate.getId());
        assertEquals(user, certificate.getUser());
        assertEquals(startDate, certificate.getStartDate());
        assertEquals(endDate, certificate.getEndDate());
        assertEquals("Justification", certificate.getJustification());
        assertArrayEquals(imageData, certificate.getImageData());
        assertEquals(CertificateStatusEnum.APROVADO, certificate.getStatus());
        assertEquals(request, certificate.getRequest());
    }

    @Test
    public void testSettersAndGetters() {
        Certificate certificate = new Certificate();

        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(2L);

        byte[] imageData = new byte[] {1, 2, 3, 4, 5};

        LocalDate startDate = LocalDate.of(2023, 3, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 31);

        certificate.setId(1L);
        certificate.setUser(user);
        certificate.setStartDate(startDate);
        certificate.setEndDate(endDate);
        certificate.setJustification("Justification");
        certificate.setImageData(imageData);
        certificate.setStatus(CertificateStatusEnum.REPROVADO);
        certificate.setRequest(request);

        assertEquals(1L, certificate.getId());
        assertEquals(user, certificate.getUser());
        assertEquals(startDate, certificate.getStartDate());
        assertEquals(endDate, certificate.getEndDate());
        assertEquals("Justification", certificate.getJustification());
        assertArrayEquals(imageData, certificate.getImageData());
        assertEquals(CertificateStatusEnum.REPROVADO, certificate.getStatus());
        assertEquals(request, certificate.getRequest());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(1L);

        byte[] imageData = new byte[] {1, 2, 3};

        Certificate cert1 = new Certificate(1L, user, LocalDate.now(), LocalDate.now().plusDays(1), "Justification",
            imageData, CertificateStatusEnum.PENDENTE, request);

        Certificate cert2 = new Certificate(1L, user, LocalDate.now(), LocalDate.now().plusDays(1), "Justification",
            imageData, CertificateStatusEnum.PENDENTE, request);

        Certificate cert3 = new Certificate(2L, user, LocalDate.now(), LocalDate.now().plusDays(1), "Justification",
            imageData, CertificateStatusEnum.PENDENTE, request);

        assertEquals(cert1, cert2);
        assertEquals(cert1.hashCode(), cert2.hashCode());
        assertNotEquals(cert1, cert3);
        assertNotEquals(cert1.hashCode(), cert3.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Request request = new Request();
        request.setId(2L);

        byte[] imageData = new byte[] {1, 2, 3};

        Certificate certificate = Certificate.builder()
            .id(1L)
            .user(user)
            .startDate(LocalDate.of(2023, 4, 1))
            .endDate(LocalDate.of(2023, 4, 30))
            .justification("Justification")
            .imageData(imageData)
            .status(CertificateStatusEnum.APROVADO)
            .request(request)
            .build();

        String result = certificate.toString();
        assertNotNull(result);
        assertTrue(result.contains("Certificate("));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("user=User("));
        assertTrue(result.contains("startDate=2023-04-01"));
        assertTrue(result.contains("endDate=2023-04-30"));
        assertTrue(result.contains("justification=Justification"));
        assertTrue(result.contains("status=APROVADO"));
        assertTrue(result.contains("request=Request("));
    }
}

package com.wisetime.wisetime.service.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateService certificateService;

    private Certificate certificate;
    private User user;
    private Request request;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("Evelyn")
                .email("evelyn@teste.com")
                .password("password")
                .build();

        request = new Request();
        request.setId(100L);

        certificate = Certificate.builder()
                .id(1L)
                .user(user)
                .startDate(LocalDate.now().minusDays(5))
                .endDate(LocalDate.now())
                .justification("Medical leave")
                .status(CertificateStatusEnum.APROVADO)
                .request(request)
                .build();
    }

    @Test
    public void testFindByRequestId_Success() {
        Long requestId = 100L;
        when(certificateRepository.findByRequestId(requestId)).thenReturn(certificate);

        Certificate result = certificateService.findByRequestId(requestId);

        assertNotNull(result, "O certificado não deve ser nulo.");
        assertEquals(certificate, result, "O certificado retornado deve ser o mesmo.");
        verify(certificateRepository).findByRequestId(requestId);
    }

    @Test
    public void testFindByRequestId_NotFound() {
        Long requestId = 200L;
        when(certificateRepository.findByRequestId(requestId)).thenReturn(null);

        Certificate result = certificateService.findByRequestId(requestId);

        assertEquals(null, result, "O resultado deve ser nulo quando o certificado não é encontrado.");
        verify(certificateRepository).findByRequestId(requestId);
    }

    @Test
    public void testSave_Success() {
        when(certificateRepository.save(any(Certificate.class))).thenReturn(certificate);

        Certificate savedCertificate = certificateService.save(certificate);

        assertNotNull(savedCertificate, "O certificado salvo não deve ser nulo.");
        assertEquals(certificate, savedCertificate, "O certificado salvo deve ser o mesmo.");
        verify(certificateRepository).save(certificate);
    }

    @Test
    public void testFindById_Success() {
        Long certificateId = 1L;
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        Certificate result = certificateService.findById(certificateId);

        assertNotNull(result, "O certificado não deve ser nulo.");
        assertEquals(certificate, result, "O certificado retornado deve ser o mesmo.");
        verify(certificateRepository).findById(certificateId);
    }

    @Test
    public void testFindById_NotFound() {
        Long certificateId = 2L;
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            certificateService.findById(certificateId);
        });

        assertEquals("Certificado não encontrado", exception.getMessage(), "A mensagem de exceção deve estar correta.");
        verify(certificateRepository).findById(certificateId);
    }
}

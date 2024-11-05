package com.wisetime.wisetime.controller.certificate;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CertificateControllerTest {

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateController certificateController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(certificateController).build();
    }

    @Test
    public void testGetCertificateImage_Found() throws Exception {
        Long certificateId = 1L;
        byte[] imageData = new byte[]{1, 2, 3}; 
        Certificate certificate = new Certificate();
        certificate.setId(certificateId);
        certificate.setImageData(imageData);

        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        mockMvc.perform(get("/api/certificate/image/{id}", certificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageData));

        verify(certificateRepository, times(1)).findById(certificateId);
    }

}

package com.wisetime.wisetime.controller.certificate;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateRepository certificateRepository;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getCertificateImage(@PathVariable Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

        byte[] imageData = certificate.getImageData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); 

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageData);
    }
}

package com.wisetime.wisetime.service.certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.repository.certificate.CertificateRepository;

import jakarta.transaction.Transactional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Transactional
    public Certificate findByRequestId(Long requestId) {
        return certificateRepository.findByRequestId(requestId);
    }

    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    public Certificate findById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));
    }

}

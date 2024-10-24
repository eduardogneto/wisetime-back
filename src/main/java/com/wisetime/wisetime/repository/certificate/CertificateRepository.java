package com.wisetime.wisetime.repository.certificate;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.certificate.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
	
	@EntityGraph(attributePaths = {"id", "justification", "startDate", "endDate", "status", "request", "user"})
    Certificate findByRequestId(Long requestId);
}

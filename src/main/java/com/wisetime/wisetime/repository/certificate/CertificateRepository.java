package com.wisetime.wisetime.repository.certificate;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
	
	@EntityGraph(attributePaths = {"id", "justification", "startDate", "endDate", "status", "request", "user"})
    Certificate findByRequestId(Long requestId);
	
	List<Certificate> findByUserIdAndStatus(Long userId, CertificateStatusEnum status);
}

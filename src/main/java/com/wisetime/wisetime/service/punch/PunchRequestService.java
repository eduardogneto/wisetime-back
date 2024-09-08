package com.wisetime.wisetime.service.punch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.models.punch.PunchRequest;
import com.wisetime.wisetime.repository.punch.PunchRequestRepository;

@Service
public class PunchRequestService {

    @Autowired
    private PunchRequestRepository punchRequestRepository;

    public PunchRequest save(PunchRequest punchRequest) {
        return punchRequestRepository.save(punchRequest);
    }

    public PunchRequest findById(Long requestId) {
        return punchRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }
}

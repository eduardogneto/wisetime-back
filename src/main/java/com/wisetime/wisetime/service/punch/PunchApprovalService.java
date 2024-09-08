package com.wisetime.wisetime.service.punch;


import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.models.punch.PunchRequest;
import com.wisetime.wisetime.repository.punch.PunchRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PunchApprovalService {

    @Autowired
    private PunchRequestRepository punchRequestRepository;

    // Aprovar ou rejeitar uma solicitação
    public PunchRequest approveOrRejectRequest(Long requestId, ApprovalRequestDTO approvalRequestDTO) {
        PunchRequest request = punchRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        request.setStatus(approvalRequestDTO.getStatus());

        return punchRequestRepository.save(request);
    }
}

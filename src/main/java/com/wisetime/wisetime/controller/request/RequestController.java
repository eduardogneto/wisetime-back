package com.wisetime.wisetime.controller.request;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.DTO.request.RequestFilterDTO;
import com.wisetime.wisetime.DTO.request.RequestResponseDTO;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.service.request.RequestService;

@RestController
@RequestMapping("/api/request")
public class RequestController {


    @Autowired
    private RequestService requestService;
    

    @PostMapping("/create")
    public ResponseEntity<RequestResponseDTO> createRequest(@RequestBody RequestDTO requestDTO) {
        Request savedRequest = this.requestService.create(requestDTO);
        RequestResponseDTO responseDTO = this.requestService.mapToDTO(savedRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<RequestDTO>> getRequests(@PathVariable Long userId) {
        List<RequestDTO> requestDTOs = requestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(requestDTOs);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<Request> approveRequest(@PathVariable Long requestId, @RequestBody ApprovalRequestDTO approvalRequestDTO) {
        Request approvedRequest = requestService.approveRequest(requestId, approvalRequestDTO);
        return ResponseEntity.ok(approvedRequest);
    }
    
    @GetMapping("/countByStatus/{organizationId}")
    public ResponseEntity<Map<String, Long>> countRequestsByStatus(@PathVariable Long organizationId) {
        Map<String, Long> counts = requestService.countRequestsByStatus(organizationId);
        return ResponseEntity.ok(counts);
    }
    
    @PostMapping("/filter")
    public ResponseEntity<List<RequestDTO>> filterRequests(@RequestBody RequestFilterDTO filterDTO) {
        List<RequestDTO> filteredRequests = requestService.getFilteredRequests(filterDTO);
        return ResponseEntity.ok(filteredRequests);
    }
    
    


}


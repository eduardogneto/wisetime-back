package com.wisetime.wisetime.service.request;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.repository.request.RequestRepository;
import com.wisetime.wisetime.request.Request;
import com.wisetime.wisetime.request.RequestStatusEnum;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Optional<Request> findById(Long requestId) {
        return requestRepository.findById(requestId);
    }

    public List<Request> findByOrganizationId(Long organizationId) {
        return requestRepository.findByOrganizationId(organizationId);
    }
    
    public Long countByOrganizationIdAndStatus(Long organizationId, RequestStatusEnum status) {
        return requestRepository.countByOrganizationIdAndStatus(organizationId, status);
    }
}

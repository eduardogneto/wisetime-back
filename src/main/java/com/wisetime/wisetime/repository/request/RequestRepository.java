package com.wisetime.wisetime.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.request.Request;
import com.wisetime.wisetime.request.RequestStatusEnum;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByOrganizationIdAndStatus(Long organizationId, RequestStatusEnum status);

    List<Request> findByOrganizationId(Long organizationId);
    
    Long countByOrganizationIdAndStatus(Long organizationId, RequestStatusEnum status);
}
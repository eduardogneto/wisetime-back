package com.wisetime.wisetime.service.organization;


import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }
}

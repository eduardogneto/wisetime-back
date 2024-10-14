package com.wisetime.wisetime.repository.organization;


import com.wisetime.wisetime.models.organization.Organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	
	public List<Organization> findAll();
}


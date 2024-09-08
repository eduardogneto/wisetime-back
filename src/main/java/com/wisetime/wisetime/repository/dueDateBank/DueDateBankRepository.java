package com.wisetime.wisetime.repository.dueDateBank;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;

@Repository
public interface DueDateBankRepository extends JpaRepository<DueDateBank, Long> {
	List<DueDateBank> findByOrganizationId(Long organizationId);
}

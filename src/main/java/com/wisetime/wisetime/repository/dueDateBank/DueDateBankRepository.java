package com.wisetime.wisetime.repository.dueDateBank;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.dueDateBank.DueDateBank;
import com.wisetime.wisetime.models.organization.Organization;

@Repository
public interface DueDateBankRepository extends JpaRepository<DueDateBank, Long> {
	List<DueDateBank> findByOrganizationId(Long organizationId);

	List<DueDateBank> findByEndDateAfter(LocalDate date);

	boolean existsByStartDateAndOrganization(LocalDate startDate, Organization organization);
	
	DueDateBank findByOrganizationIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long organizationId, LocalDate date1, LocalDate date2);

    DueDateBank findTopByOrganizationIdAndEndDateLessThanOrderByEndDateDesc(
            Long organizationId, LocalDate date);
	
    DueDateBank findTopByOrganizationIdOrderByStartDateAsc(Long organizationId);
	
}

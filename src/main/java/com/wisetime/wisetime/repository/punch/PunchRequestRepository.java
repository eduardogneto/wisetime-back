package com.wisetime.wisetime.repository.punch;


import com.wisetime.wisetime.models.punch.PunchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunchRequestRepository extends JpaRepository<PunchRequest, Long> {
}

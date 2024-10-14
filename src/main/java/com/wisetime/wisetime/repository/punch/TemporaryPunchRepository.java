package com.wisetime.wisetime.repository.punch;

import com.wisetime.wisetime.models.punch.TemporaryPunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryPunchRepository extends JpaRepository<TemporaryPunch, Long> {
    
    List<TemporaryPunch> findByUserId(Long userId);
    
    List<TemporaryPunch> findByRequestId(Long requestId);
}

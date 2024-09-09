package com.wisetime.wisetime.repository.punch;

import com.wisetime.wisetime.models.punch.TemporaryPunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryPunchRepository extends JpaRepository<TemporaryPunch, Long> {
    
    // Buscar punches temporários por ID do usuário
    List<TemporaryPunch> findByUserId(Long userId);
    
    // Buscar punches temporários por ID da solicitação
    List<TemporaryPunch> findByRequestId(Long requestId);
}

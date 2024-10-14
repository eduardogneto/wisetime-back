package com.wisetime.wisetime.repository.punch;


import com.wisetime.wisetime.models.punch.PunchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PunchLogRepository extends JpaRepository<PunchLog, Long> {

    List<PunchLog> findByUserIdAndTimestampBetween(Long userId, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
    
    List<PunchLog> findByUserId(Long userId);
    
    // Para obter a última batida de ponto
    @Query("SELECT p FROM PunchLog p WHERE p.user.id = :userId ORDER BY p.timestamp DESC")
    List<PunchLog> findLastPunchLogByUserId(@Param("userId") Long userId);
    
    // Para encontrar as batidas subsequentes a partir de uma data
    @Query("SELECT p FROM PunchLog p WHERE p.user.id = :userId AND p.timestamp > :timestamp ORDER BY p.timestamp ASC")
    List<PunchLog> findSubsequentPunchLogs(@Param("userId") Long userId, @Param("timestamp") LocalDateTime timestamp);
    
    List<PunchLog> findByUserIdAndOrganizationId(Long userId, Long organizationId);
    
    List<PunchLog> findByUserIdAndTimestampBefore(Long userId, LocalDateTime timestamp);

    // Método para obter o primeiro PunchLog de um usuário (mais antigo)
    PunchLog findFirstByUserIdOrderByTimestampAsc(Long userId);
}


package com.wisetime.wisetime.repository.request;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.request.RequestStatusEnum;
import com.wisetime.wisetime.models.request.RequestTypeEnum;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByOrganizationIdAndStatus(Long organizationId, RequestStatusEnum status);

    List<Request> findByOrganizationId(Long organizationId);
    
    List<Request> findByUserId(Long userId);
    
    
    
    @Query("SELECT COUNT(r) FROM Request r WHERE r.user.team.id = :teamId AND r.status = :status")
    Long countByTeamIdAndStatus(@Param("teamId") Long teamId, @Param("status") RequestStatusEnum status);
    
    @Query("SELECT r FROM Request r WHERE r.user.team.id = :teamId")
    List<Request> findByUserTeamId(@Param("teamId") Long teamId);

    @EntityGraph(attributePaths = {
    	    "user",
    	    "temporaryPunches",
    	    "certificate",
    	    "certificate.startDate",
    	    "certificate.endDate",
    	    "certificate.justification",
    	    "certificate.status"
    	})
    	List<Request> findByUserTeamIdAndRequestTypeInAndStatusIn(
    	    Long teamId, List<RequestTypeEnum> requestTypes, List<RequestStatusEnum> statuses);

    
    @Query("SELECT r FROM Request r WHERE r.user.team.id = :teamId AND r.requestType IN :requestTypes")
    List<Request> findByUserTeamIdAndRequestTypeIn(@Param("teamId") Long teamId, @Param("requestTypes") List<RequestTypeEnum> requestTypes);

    @Query("SELECT r FROM Request r WHERE r.user.team.id = :teamId AND r.status IN :statuses")
    List<Request> findByUserTeamIdAndStatusIn(@Param("teamId") Long teamId, @Param("statuses") List<RequestStatusEnum> statuses);
    
}
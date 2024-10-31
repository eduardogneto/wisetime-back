package com.wisetime.wisetime.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	UserDetails findByEmail(String email);
	
    Optional<User> findById(Long userId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.team.organization.id = :organizationId")
    Long countByOrganizationId(@Param("organizationId") Long organizationId);
    
    List<User> findByTeam_Organization_Id(Long organizationId);
}

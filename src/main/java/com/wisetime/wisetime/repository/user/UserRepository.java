package com.wisetime.wisetime.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.wisetime.wisetime.models.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByEmail(String email);
    
	UserDetails findByEmail(String email);
	
    Optional<User> findById(Long userId);
    
    List<User> findByTeam_Organization_Id(Long organizationId);
}

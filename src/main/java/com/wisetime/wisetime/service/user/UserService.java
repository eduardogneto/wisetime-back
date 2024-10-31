package com.wisetime.wisetime.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.user.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public Optional<User> findEntityById(Long userId) {
        return userRepository.findById(userId); 
    }

    public List<UserResponseDTO> getUsersByOrganization(Long organizationId) {
        List<User> users = userRepository.findByTeam_Organization_Id(organizationId);
        return users.stream().map(this::mapToDTO).collect(Collectors.toList()); 
    }

    public UserResponseDTO mapToDTO(User user) {
        Team team = user.getTeam();
        TeamDTO teamDTO = null;

        if (team != null) {
            Organization organization = team.getOrganization();
            Long organizationId = (organization != null) ? organization.getId() : null;

            teamDTO = new TeamDTO(
                team.getId(),
                team.getName(),
                team.getDescription(),
                organizationId
            );
        }

        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            teamDTO,
            user.getTag()
        );
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

	public Long getEmployees(Long organizationId) {
		if(organizationId == null) {
			return 0L;
		}
		
		Long count = userRepository.countByOrganizationId(organizationId);
		if (count == null) {
			return 0L;
		}
		return count;
	}

    
}

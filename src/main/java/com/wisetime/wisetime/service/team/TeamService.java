package com.wisetime.wisetime.service.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.repository.team.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<TeamDTO> getTeamsByOrganization(Long organizationId) {
        List<Team> teams = teamRepository.findByOrganizationId(organizationId);
        return teams.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private TeamDTO mapToDTO(Team team) {
        Long organizationId = null;
        if (team.getOrganization() != null) {
            organizationId = team.getOrganization().getId();
        }
        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getDescription(),
            organizationId
        );
    }
    
    
    
}

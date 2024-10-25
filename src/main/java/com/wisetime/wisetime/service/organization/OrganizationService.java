package com.wisetime.wisetime.service.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.organzation.AddressDTO;
import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.service.audit.AuditService;
import com.wisetime.wisetime.service.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private UserService userService;

    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }
    

    public List<OrganizationDTO> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrganizationDTO convertToDTO(Organization organization) {
        return new OrganizationDTO(
            organization.getId(),
            organization.getName(),
            organization.getTaxId(),
            organization.getEmail(),
            organization.getPhone(),
            convertAddressToDTO(organization.getAddress()),
            organization.getTeams().stream()
                .map(this::convertTeamToDTO)
                .collect(Collectors.toList())
        );
    }

    private AddressDTO convertAddressToDTO(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getCity(),
            address.getState(),
            address.getZipCode()
        );
    }

    private TeamDTO convertTeamToDTO(Team team) {
        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getOrganization().getId()
        );
    }

    public Organization convertToEntity(OrganizationDTO organizationDTO) {
        Address address = convertAddressDTOToEntity(organizationDTO.getAddress());

        Organization organization = new Organization(
            organizationDTO.getId(),
            organizationDTO.getName(),
            organizationDTO.getTaxId(),
            organizationDTO.getEmail(),
            organizationDTO.getPhone(),
            address
        );

        List<Team> teams = new ArrayList<>();
        if (organizationDTO.getTeams() != null) {
            for (TeamDTO teamDTO : organizationDTO.getTeams()) {
                Team team = new Team(
                    teamDTO.getName(),
                    teamDTO.getDescription(),
                    organization
                );
                teams.add(team);
            }
        }
        organization.setTeams(teams);

        return organization;
    }

    private Address convertAddressDTOToEntity(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }
        return new Address(
            addressDTO.getStreet(),
            addressDTO.getNumber(),
            addressDTO.getComplement(),
            addressDTO.getCity(),
            addressDTO.getState(),
            addressDTO.getZipCode()
        );
    }

    public void saveOrganization(Organization organization, OrganizationDTO organizationDTO) {
    	User user = userService.findEntityById(organizationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + organizationDTO.getUserId()));
        organizationRepository.save(organization);
        auditService.logAction("Criou Organização", user, "O Administrador criou uma organização chamada: " + organizationDTO.getName());
        
    }

    public Organization updateOrganization(Organization existingOrganization, OrganizationDTO organizationDTO) {
    	User user = userService.findEntityById(organizationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + organizationDTO.getUserId()));
    	
        existingOrganization.setName(organizationDTO.getName());
        existingOrganization.setTaxId(organizationDTO.getTaxId());
        existingOrganization.setEmail(organizationDTO.getEmail());
        existingOrganization.setPhone(organizationDTO.getPhone());

        Address updatedAddress = convertAddressDTOToEntity(organizationDTO.getAddress());
        existingOrganization.setAddress(updatedAddress);

        List<Team> updatedTeams = new ArrayList<>();
        if (organizationDTO.getTeams() != null) {
            for (TeamDTO teamDTO : organizationDTO.getTeams()) {
                Team team = new Team(
                    teamDTO.getName(),
                    teamDTO.getDescription(),
                    existingOrganization
                );
                updatedTeams.add(team);
            }
        }
        existingOrganization.setTeams(updatedTeams);
        auditService.logAction("Atualizou Organização", user, "O Administrador atualizou uma organização chamada: " + organizationDTO.getName());
        return existingOrganization;
    }
    
    @Transactional
    public void updateTeams(Organization organization, List<TeamDTO> teamsDTO) {
        Map<Long, Team> existingTeamsMap = organization.getTeams().stream()
            .collect(Collectors.toMap(Team::getId, team -> team));

        List<Team> updatedTeams = new ArrayList<>();

        for (TeamDTO dto : teamsDTO) {
            if (dto.getId() != null && existingTeamsMap.containsKey(dto.getId())) {
                Team team = existingTeamsMap.get(dto.getId());
                team.setName(dto.getName());
                team.setDescription(dto.getDescription());
                updatedTeams.add(team);
            } else {
                Team newTeam = new Team();
                newTeam.setName(dto.getName());
                newTeam.setDescription(dto.getDescription());
                newTeam.setOrganization(organization);
                updatedTeams.add(newTeam);
            }
        }

        Set<Long> updatedTeamIds = teamsDTO.stream()
            .filter(dto -> dto.getId() != null)
            .map(TeamDTO::getId)
            .collect(Collectors.toSet());

        List<Team> teamsToRemove = organization.getTeams().stream()
            .filter(team -> !updatedTeamIds.contains(team.getId()))
            .collect(Collectors.toList());

        for (Team team : teamsToRemove) {
            organization.getTeams().remove(team);
            teamRepository.delete(team);
        }

        organization.setTeams(updatedTeams);
        organizationRepository.save(organization);
    }
    
}

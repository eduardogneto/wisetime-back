// Pacote: com.wisetime.wisetime.controllers
package com.wisetime.wisetime.controller.organization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;
import com.wisetime.wisetime.service.organization.OrganizationService;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private OrganizationService organizationService;
    
    @GetMapping("/allOrganizations") 
    public List<OrganizationDTO> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }


    @PostMapping("/organizations")
    public ResponseEntity<?> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        Address address = new Address(
            organizationDTO.getAddress().getStreet(),
            organizationDTO.getAddress().getNumber(),
            organizationDTO.getAddress().getComplement(),
            organizationDTO.getAddress().getCity(),
            organizationDTO.getAddress().getState(),
            organizationDTO.getAddress().getZipCode()
        );

        Organization organization = new Organization(
            organizationDTO.getName(),
            organizationDTO.getTaxId(),
            organizationDTO.getEmail(),
            organizationDTO.getPhone(),
            address
        );

        // Converter TeamDTO para Team e associar à organização
        List<Team> teams = new ArrayList<>();
        if (organizationDTO.getTeams() != null) {
            for (TeamDTO teamDTO : organizationDTO.getTeams()) {
                Team team = new Team(
                    teamDTO.getName(),
                    teamDTO.getDescription(),
                    organization // Associação bidirecional
                );
                teams.add(team);
            }
        }
        organization.setTeams(teams);

        organizationRepository.save(organization);

        return ResponseEntity.ok("Organização criada com sucesso");
    }
}

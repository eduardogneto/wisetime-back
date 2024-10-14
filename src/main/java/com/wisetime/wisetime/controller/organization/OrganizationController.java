package com.wisetime.wisetime.controller.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.service.organization.OrganizationService;
import com.wisetime.wisetime.service.team.TeamService;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private TeamService teamService;

    @GetMapping("/allOrganizations") 
    public List<OrganizationDTO> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long id) {
        Optional<Organization> optionalOrganization = organizationService.findById(id);
        if (!optionalOrganization.isPresent()) {
            return ResponseEntity.status(404).body("Organização não encontrada");
        }
        OrganizationDTO organizationDTO = organizationService.convertToDTO(optionalOrganization.get());
        return ResponseEntity.ok(organizationDTO);
    }

    @PostMapping("/organizations")
    public ResponseEntity<?> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        Organization organization = organizationService.convertToEntity(organizationDTO);
        organizationService.saveOrganization(organization);
        OrganizationDTO savedOrganizationDTO = organizationService.convertToDTO(organization);
        return ResponseEntity.ok(savedOrganizationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long id, @RequestBody OrganizationDTO organizationDTO) {
        Optional<Organization> optionalOrganization = organizationService.findById(id);
        if (!optionalOrganization.isPresent()) {
            return ResponseEntity.status(404).body("Organização não encontrada");
        }

        Organization updatedOrganization = organizationService.updateOrganization(optionalOrganization.get(), organizationDTO);
        organizationService.saveOrganization(updatedOrganization);

        OrganizationDTO updatedOrganizationDTO = organizationService.convertToDTO(updatedOrganization);
        return ResponseEntity.ok(updatedOrganizationDTO);
    }
    
    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TeamDTO>> getTeamsByOrganization(@PathVariable Long id) {
        List<TeamDTO> teams = teamService.getTeamsByOrganization(id);
        if (teams.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(teams);
    }
    
    @PutMapping("/{id}/teams")
    public ResponseEntity<?> updateTeams(@PathVariable Long id, @RequestBody List<TeamDTO> teamsDTO) {
        Optional<Organization> optionalOrganization = organizationService.findById(id);
        if (!optionalOrganization.isPresent()) {
            return ResponseEntity.status(404).body("Organização não encontrada");
        }
        try {
            organizationService.updateTeams(optionalOrganization.get(), teamsDTO);
            return ResponseEntity.ok("Times atualizados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Erro ao atualizar times");
        }
    }
    
}

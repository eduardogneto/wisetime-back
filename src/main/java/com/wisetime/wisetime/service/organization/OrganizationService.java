package com.wisetime.wisetime.service.organization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.organzation.OrganizationDTO;
import com.wisetime.wisetime.DTO.organzation.AddressDTO;
import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.repository.organization.OrganizationRepository;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    // Método para buscar todas as organizações
    public List<OrganizationDTO> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversão da entidade Organization para OrganizationDTO
    private OrganizationDTO convertToDTO(Organization organization) {
        return new OrganizationDTO(
            organization.getName(),
            organization.getTaxId(),
            organization.getEmail(),
            organization.getPhone(),
            convertAddressToDTO(organization.getAddress()), // Converter Address para AddressDTO
            organization.getTeams().stream().map(team -> new TeamDTO(
                team.getId(),team.getName(), team.getDescription(), team.getOrganization().getId()
            )).collect(Collectors.toList())
        );
    }

    // Método para converter Address para AddressDTO
    private AddressDTO convertAddressToDTO(Address address) {
        return new AddressDTO(
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getCity(),
            address.getState(),
            address.getZipCode()
        );
    }
}

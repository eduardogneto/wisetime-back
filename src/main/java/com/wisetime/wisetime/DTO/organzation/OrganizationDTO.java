package com.wisetime.wisetime.DTO.organzation;

import java.util.List;

import com.wisetime.wisetime.DTO.team.TeamDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
	
	private Long id;
	private Long userId;
    private String name;
    private String taxId;
    private String email;
    private String phone;
    private AddressDTO address;
    private List<TeamDTO> teams;

    public OrganizationDTO(Long id, String name, String taxId, String email, String phone, AddressDTO address, List<TeamDTO> teams) {
    	this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.teams = teams;
    }
    
    
}

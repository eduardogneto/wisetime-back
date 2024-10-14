package com.wisetime.wisetime.DTO.organzation;

import java.util.List;
import com.wisetime.wisetime.DTO.team.TeamDTO;

public class OrganizationDTO {
	
	private Long id;
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
    
    public OrganizationDTO(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }
}

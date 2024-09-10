package com.wisetime.wisetime.DTO.team;

public class TeamDTO {
    private Long id;
    private String name;
    private Long organizationId;

    public TeamDTO(Long id, String name, Long organizationId) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}

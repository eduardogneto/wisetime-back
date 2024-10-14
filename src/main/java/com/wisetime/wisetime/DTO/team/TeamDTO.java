package com.wisetime.wisetime.DTO.team;

public class TeamDTO {
    private Long id;
    private String name;
    private String description;
    private Long organizationId;

    public TeamDTO() {
    }

    public TeamDTO(Long id, String name, String description, Long organizationId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organizationId = organizationId;
    }
    
    public TeamDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}

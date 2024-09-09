package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.models.user.TagUserEnum;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Long organizationId;
    private TagUserEnum tag;

    // Construtor
    public UserResponseDTO(Long id, String name, String email, Long organizationId, TagUserEnum tag) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.organizationId = organizationId;
        this.tag = tag;
    }

    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public TagUserEnum getTag() {
		return tag;
	}

	public void setTag(TagUserEnum tag) {
		this.tag = tag;
	}

	public Long getOrganization_id() {
		return organizationId;
	}

	public void setOrganization_id(Long organizationId) {
		this.organizationId = organizationId;
	}

	// Getters e setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.wisetime.wisetime.DTO.user;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Long organizationId;

    // Construtor
    public UserResponseDTO(Long id, String name, String email, Long organizationId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.organizationId = organizationId;
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

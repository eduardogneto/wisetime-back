package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;
import com.wisetime.wisetime.DTO.user.UserDTO;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;

public class PunchLogResponseDTO {
    private Long id;
    private UserDTO user;
    private LocalDateTime timestamp;
    private PunchTypeEnum type;
    private String location;
    private Long organizationId; 

    public PunchLogResponseDTO() {}

    public PunchLogResponseDTO(Long id, UserDTO user, LocalDateTime timestamp, PunchTypeEnum type, String location, Long organizationId) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
        this.location = location;
        this.organizationId = organizationId;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public PunchTypeEnum getType() {
		return type;
	}

	public void setType(PunchTypeEnum type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

    
}

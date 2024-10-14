package com.wisetime.wisetime.models.punch;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "punch_logs")
public class PunchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum type;

    @Column(name = "location", nullable = true)
    private String location;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
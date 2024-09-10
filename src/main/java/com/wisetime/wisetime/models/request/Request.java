package com.wisetime.wisetime.models.request;

import java.util.List;

import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestTypeEnum requestType;

    @Column(name = "justification")
    private String justification;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;

    @OneToMany(mappedBy = "request")
    private List<TemporaryPunch> temporaryPunches;

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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public RequestTypeEnum getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypeEnum requestType) {
		this.requestType = requestType;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public RequestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(RequestStatusEnum status) {
		this.status = status;
	}

	public List<TemporaryPunch> getTemporaryPunches() {
		return temporaryPunches;
	}

	public void setTemporaryPunches(List<TemporaryPunch> temporaryPunches) {
		this.temporaryPunches = temporaryPunches;
	}

    // Getters e setters...
}


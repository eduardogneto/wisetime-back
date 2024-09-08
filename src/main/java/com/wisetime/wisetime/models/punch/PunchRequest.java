package com.wisetime.wisetime.models.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "punch_requests")
public class PunchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punch_log_id", nullable = false)
    private PunchLog punchLog;

    @Column(name = "new_timestamp", nullable = false)
    private LocalDateTime newTimestamp;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatusEnum status;

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

	public PunchLog getPunchLog() {
		return punchLog;
	}

	public void setPunchLog(PunchLog punchLog) {
		this.punchLog = punchLog;
	}

	public LocalDateTime getNewTimestamp() {
		return newTimestamp;
	}

	public void setNewTimestamp(LocalDateTime newTimestamp) {
		this.newTimestamp = newTimestamp;
	}

	public ApprovalStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatusEnum status) {
		this.status = status;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    // Getters e Setters
}


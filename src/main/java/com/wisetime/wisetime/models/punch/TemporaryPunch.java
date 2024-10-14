package com.wisetime.wisetime.models.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.*;

@Entity
@Table(name = "temporary_punches")
public class TemporaryPunch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request; 
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum type;  

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

    public PunchTypeEnum getStatus() {
        return status;
    }

    public void setStatus(PunchTypeEnum status) {
        this.status = status;
    }

    public PunchTypeEnum getType() {
		return type;
	}

	public void setType(PunchTypeEnum type) {
		this.type = type;
	}

	public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}


package com.wisetime.wisetime.DTO.request;

import java.util.List;

import com.wisetime.wisetime.DTO.punch.PunchDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;

public class RequestDTO {
	private Long id;
    private String justification;
    private String requestType;
    private String status;
    private UserDTO user; 
    private List<PunchDTO> punches; 
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public List<PunchDTO> getPunches() {
		return punches;
	}
	public void setPunches(List<PunchDTO> punches) {
		this.punches = punches;
	}

    
}

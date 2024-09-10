package com.wisetime.wisetime.DTO.request;

import java.util.List;

import com.wisetime.wisetime.DTO.punch.TemporaryPunchDTO;


public class RequestResponseDTO {
    private Long id;
    private String requestType;
    private String justification;
    private String status;
    private List<TemporaryPunchDTO> punches;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TemporaryPunchDTO> getPunches() {
		return punches;
	}
	public void setPunches(List<TemporaryPunchDTO> punches) {
		this.punches = punches;
	}

    
}


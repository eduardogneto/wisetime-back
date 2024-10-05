package com.wisetime.wisetime.DTO.request;

import java.util.List;

public class RequestFilterDTO {
	private Long teamId;
    private List<String> types;  
    private List<String> statuses; 


    public List<String> getTypes() {
        return types;
    }

    public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long userId) {
		this.teamId = userId;
	}

	public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }
}

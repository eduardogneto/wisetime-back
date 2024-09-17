package com.wisetime.wisetime.DTO.request;

import java.util.List;

public class RequestFilterDTO {
	private Long userId;
    private List<String> types;  
    private List<String> statuses; 


    public List<String> getTypes() {
        return types;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

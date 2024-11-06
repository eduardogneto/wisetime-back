package com.wisetime.wisetime.DTO.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFilterDTO {
	private Long teamId;
    private List<String> types;  
    private List<String> statuses; 
}

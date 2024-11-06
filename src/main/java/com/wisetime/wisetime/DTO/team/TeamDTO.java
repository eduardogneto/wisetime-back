package com.wisetime.wisetime.DTO.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor	
public class TeamDTO {
    private Long id;
    private String name;
    private String description;
    private Long organizationId;
}

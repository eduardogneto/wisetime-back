package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.user.TagUserEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private TeamDTO team; 
    private TagUserEnum tag;

  
}

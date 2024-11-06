package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.models.user.TagUserEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private Long id; 
    private String name;
    private String email;
    private String password;
    private Long teamId; 
    private TagUserEnum tag; 
}

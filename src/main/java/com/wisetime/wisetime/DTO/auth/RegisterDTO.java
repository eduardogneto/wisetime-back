package com.wisetime.wisetime.DTO.auth;

import com.wisetime.wisetime.models.user.TagUserEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {

    private String email; 
    private String password;
    private TagUserEnum tag;
    private String name;  
    private Long teamId; 

   
}

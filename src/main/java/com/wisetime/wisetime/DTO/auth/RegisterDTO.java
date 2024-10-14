package com.wisetime.wisetime.DTO.auth;

import com.wisetime.wisetime.models.user.TagUserEnum;

public class RegisterDTO {

    private String email; 
    private String password;
    private TagUserEnum tag;
    private String name;  
    private Long teamId; 

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public TagUserEnum getTag() {
        return tag;
    }
    
    public void setTag(TagUserEnum tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}

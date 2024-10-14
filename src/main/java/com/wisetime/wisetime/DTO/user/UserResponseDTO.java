package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.models.user.TagUserEnum;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private TeamDTO team; 
    private TagUserEnum tag;

    public UserResponseDTO(Long id, String name, String email, TeamDTO team, TagUserEnum tag) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.team = team;
        this.tag = tag;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void getTeam(TeamDTO team) {
        this.team = team;
    }

    public TagUserEnum getTag() {
        return tag;
    }

    public void setTag(TagUserEnum tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

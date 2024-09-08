package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.models.user.TagUserEnum;

public class UserRegisterDTO {

    private Long id; // Campo para armazenar o ID do usuário em caso de atualização
    private String name;
    private String email;
    private String password;
    private Long roleId; // Armazena apenas o ID da Role
    private TagUserEnum tag; // Adicionado o campo tag

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
}

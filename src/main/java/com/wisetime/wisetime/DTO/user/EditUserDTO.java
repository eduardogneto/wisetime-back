package com.wisetime.wisetime.DTO.user;

import com.wisetime.wisetime.models.user.TagUserEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditUserDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private String password;

    @NotNull(message = "Tag é obrigatória")
    private TagUserEnum tag;

    @NotNull(message = "ID do time é obrigatório")
    private Long teamId;
    
}

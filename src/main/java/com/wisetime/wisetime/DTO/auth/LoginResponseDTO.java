package com.wisetime.wisetime.DTO.auth;

import com.wisetime.wisetime.DTO.user.UserResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private UserResponseDTO user;


}

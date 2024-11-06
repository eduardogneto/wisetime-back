package com.wisetime.wisetime.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {
	private String email; 
	private String password;
}

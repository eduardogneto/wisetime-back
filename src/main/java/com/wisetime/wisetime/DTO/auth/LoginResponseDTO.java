package com.wisetime.wisetime.DTO.auth;

public class LoginResponseDTO {
	
	String token;

	public LoginResponseDTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}

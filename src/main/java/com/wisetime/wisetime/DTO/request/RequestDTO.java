package com.wisetime.wisetime.DTO.request;

import java.util.List;

import com.wisetime.wisetime.DTO.certificate.CertificateDTO;
import com.wisetime.wisetime.DTO.punch.PunchDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;

import lombok.Data;

@Data
public class RequestDTO {
	private Long id;
    private String justification;
    private String requestType;
    private String status;
    private UserDTO user; 
    private List<PunchDTO> punches; 
    private CertificateDTO certificate;
}

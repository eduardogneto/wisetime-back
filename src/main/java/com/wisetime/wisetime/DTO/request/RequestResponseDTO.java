package com.wisetime.wisetime.DTO.request;

import java.util.List;

import com.wisetime.wisetime.DTO.certificate.CertificateDTO;
import com.wisetime.wisetime.DTO.punch.TemporaryPunchDTO;
import com.wisetime.wisetime.DTO.user.UserDTO;

import lombok.Data;

@Data
public class RequestResponseDTO {
    private Long id;
    private String requestType;
    private String justification;
    private String status;
    private UserDTO user;
    private List<TemporaryPunchDTO> punches;
    private CertificateDTO certificate;
}


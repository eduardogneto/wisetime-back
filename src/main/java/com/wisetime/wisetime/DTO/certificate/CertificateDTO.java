package com.wisetime.wisetime.DTO.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
	private Long id;
    private String startDate; 
    private String endDate;
    private String justification;
    private String imageBase64;
    private String status;

}

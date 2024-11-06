package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.DTO.user.UserDTO;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchLogResponseDTO {
    private Long id;
    private UserDTO user;
    private LocalDateTime timestamp;
    private PunchTypeEnum type;
    private String location;
    private Long organizationId; 
}

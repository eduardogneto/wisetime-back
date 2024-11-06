package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.punch.PunchTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunchLogDTO {

    private Long userId;
    private LocalDateTime timestamp;
    private PunchTypeEnum type;
    private String location;

    
}

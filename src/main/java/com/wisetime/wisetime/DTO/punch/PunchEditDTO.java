package com.wisetime.wisetime.DTO.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.punch.PunchTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchEditDTO {
    private Long userId;
    private Long punchLogId;
    private LocalDateTime newTimestamp;
    private PunchTypeEnum type; 
    private String location;

}

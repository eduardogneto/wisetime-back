package com.wisetime.wisetime.DTO.punch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchSummaryDTO {
    private String date;
    private int entryCount;
    private int exitCount;
    private String status;
    
}

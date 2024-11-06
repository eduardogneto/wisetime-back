package com.wisetime.wisetime.DTO.punch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryPunchDTO {
    private String hours;
    private String status;
}
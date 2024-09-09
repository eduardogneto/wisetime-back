package com.wisetime.wisetime.DTO.punch;


public class PunchDTO {
    private String status;  // Entrada ou Saída
    private String hours;   // Horário no formato HH:mm

    // Getters e Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}


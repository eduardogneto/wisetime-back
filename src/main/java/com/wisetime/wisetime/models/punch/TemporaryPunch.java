package com.wisetime.wisetime.models.punch;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "temporary_punches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemporaryPunch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request; 
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum type;  
}

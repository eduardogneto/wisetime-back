package com.wisetime.wisetime.models.punch;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
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
@Table(name = "punch_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PunchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "America/Sao_Paulo")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PunchTypeEnum type;

    @Column(name = "location", nullable = true)
    private String location;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}
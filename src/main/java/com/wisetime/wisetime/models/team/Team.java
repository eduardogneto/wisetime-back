package com.wisetime.wisetime.models.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wisetime.wisetime.models.organization.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "team")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonIgnore
    
    private Organization organization;
    public Team(String name, String description, Organization organization) {
        this.name = name;
        this.description = description;
        this.organization = organization;
    }
}

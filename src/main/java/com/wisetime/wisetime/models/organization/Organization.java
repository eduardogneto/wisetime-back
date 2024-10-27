package com.wisetime.wisetime.models.organization;

import java.util.List;

import com.wisetime.wisetime.models.team.Team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String taxId;

    private String email;

    private String phone;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Team> teams;
    public Organization(Long id, String name, String taxId, String email, String phone, Address address) {
    	this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}

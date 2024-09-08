package com.wisetime.wisetime.models.organization;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wisetime.wisetime.models.role.Role;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    public Organization() {}

    public Organization(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

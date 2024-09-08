package com.wisetime.wisetime.models.user;

import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.role.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private TagUserEnum tag;

    public User() {}

    public User(String name, Role role, TagUserEnum tag, String email, String password) {
        this.name = name;
        this.role = role;
        this.tag = tag;
        this.email = email;
        this.password = password;
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

    public Role getRole() {
        return role;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
        this.role = role;
    }

    public TagUserEnum getTag() {
        return tag;
    }

    public void setTag(TagUserEnum tag) {
        this.tag = tag;
    }

}

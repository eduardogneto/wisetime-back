package com.wisetime.wisetime.models.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wisetime.wisetime.models.team.Team;

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

@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private TagUserEnum tag;

    public User() {}

    public User(String name, Team team, TagUserEnum tag, String email, String password) {
        this.name = name;
        this.team = team;
        this.tag = tag;
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String password, TagUserEnum tag) {
        this.email = email;
        this.password = password;
        this.tag = tag;
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

    public Team getTeam() {
        return team;
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

	public void setTeam(Team team) {
        this.team = team;
    }

    public TagUserEnum getTag() {
        return tag;
    }

    public void setTag(TagUserEnum tag) {
        this.tag = tag;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tag == TagUserEnum.ADMINISTRADOR) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
            );
        } else if (this.tag == TagUserEnum.COORDENADOR) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_COORDENATOR"),
                new SimpleGrantedAuthority("ROLE_USER")
            );
        } else if (this.tag == TagUserEnum.FUNCIONARIO) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of();
    }

	@Override
	public String getUsername() {
		return email;
	}

}

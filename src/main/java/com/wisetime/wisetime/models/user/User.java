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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

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

package com.wisetime.wisetime.models.user;

import com.wisetime.wisetime.models.team.Team;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Development");

        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .team(team)
                .tag(TagUserEnum.ADMINISTRADOR)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(team, user.getTeam());
        assertEquals(TagUserEnum.ADMINISTRADOR, user.getTag());
    }

    @Test
    public void testUserSetters() {
        User user = new User();
        Team team = new Team();

        user.setId(2L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("pass456");
        user.setTeam(team);
        user.setTag(TagUserEnum.COORDENADOR);

        assertEquals(2L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("pass456", user.getPassword());
        assertEquals(team, user.getTeam());
        assertEquals(TagUserEnum.COORDENADOR, user.getTag());
    }

    @Test
    public void testUserAuthorities_Admin() {
        User user = User.builder()
                .tag(TagUserEnum.ADMINISTRADOR)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testUserAuthorities_Coordinator() {
        User user = User.builder()
                .tag(TagUserEnum.COORDENADOR)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_COORDENATOR")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testUserAuthorities_Employee() {
        User user = User.builder()
                .tag(TagUserEnum.FUNCIONARIO)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testUserGetUsername() {
        User user = User.builder()
                .email("email@example.com")
                .build();

        assertEquals("email@example.com", user.getUsername());
    }
}

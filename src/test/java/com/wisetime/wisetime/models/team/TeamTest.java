package com.wisetime.wisetime.models.team;

import com.wisetime.wisetime.models.organization.Organization;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    @Test
    public void testTeamCreation() {
        Organization organization = new Organization();
        organization.setId(1L);

        Team team = Team.builder()
                .id(1L)
                .name("Development")
                .description("Development Team")
                .organization(organization)
                .build();

        assertEquals(1L, team.getId());
        assertEquals("Development", team.getName());
        assertEquals("Development Team", team.getDescription());
        assertEquals(organization, team.getOrganization());
    }

    @Test
    public void testTeamSetters() {
        Team team = new Team();

        team.setId(2L);
        team.setName("Marketing");
        team.setDescription("Marketing Team");
        
        Organization organization = new Organization();
        organization.setId(2L);
        team.setOrganization(organization);

        assertEquals(2L, team.getId());
        assertEquals("Marketing", team.getName());
        assertEquals("Marketing Team", team.getDescription());
        assertEquals(organization, team.getOrganization());
    }
}

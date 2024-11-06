package com.wisetime.wisetime.models.organization;

import com.wisetime.wisetime.models.team.Team;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class OrganizationTest {

    @Test
    public void testOrganizationCreation() {
        Address address = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        Organization org = new Organization(1L, "OrgName", "123456789", "org@example.com", "123-456-7890", address);

        assertEquals(1L, org.getId());
        assertEquals("OrgName", org.getName());
        assertEquals("123456789", org.getTaxId());
        assertEquals("org@example.com", org.getEmail());
        assertEquals("123-456-7890", org.getPhone());
        assertEquals(address, org.getAddress());
    }

    @Test
    public void testOrganizationSetters() {
        Organization org = new Organization();
        Address address = new Address("Elm St", "456", "Suite B", "Chicago", "IL", "60616");

        org.setId(2L);
        org.setName("UpdatedOrg");
        org.setTaxId("987654321");
        org.setEmail("updated@example.com");
        org.setPhone("098-765-4321");
        org.setAddress(address);

        assertEquals(2L, org.getId());
        assertEquals("UpdatedOrg", org.getName());
        assertEquals("987654321", org.getTaxId());
        assertEquals("updated@example.com", org.getEmail());
        assertEquals("098-765-4321", org.getPhone());
        assertEquals(address, org.getAddress());
    }

    @Test
    public void testOrganizationWithTeams() {
        Address address = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        Team team1 = new Team("Team1", "Description1", null);
        Team team2 = new Team("Team2", "Description2", null);
        Organization org = new Organization(1L, "OrgName", "123456789", "org@example.com", "123-456-7890", address);
        org.setTeams(List.of(team1, team2));

        assertEquals(2, org.getTeams().size());
        assertEquals("Team1", org.getTeams().get(0).getName());
        assertEquals("Team2", org.getTeams().get(1).getName());
    }

    @Test
    public void testOrganizationEquality() {
        Address address1 = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        Address address2 = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        
        Organization org1 = new Organization(1L, "OrgName", "123456789", "org@example.com", "123-456-7890", address1);
        Organization org2 = new Organization(1L, "OrgName", "123456789", "org@example.com", "123-456-7890", address2);

        assertEquals(org1, org2);
    }
}

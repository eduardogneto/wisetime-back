package com.wisetime.wisetime.models.punch;

import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.user.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class PunchLogTest {

    @Test
    public void testPunchLogCreation() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");

        LocalDateTime timestamp = LocalDateTime.now();
        PunchLog punchLog = PunchLog.builder()
                .id(1L)
                .user(user)
                .timestamp(timestamp)
                .type(PunchTypeEnum.ENTRY)
                .location("Office")
                .organization(organization)
                .build();

        assertEquals(1L, punchLog.getId());
        assertEquals(user, punchLog.getUser());
        assertEquals(timestamp, punchLog.getTimestamp());
        assertEquals(PunchTypeEnum.ENTRY, punchLog.getType());
        assertEquals("Office", punchLog.getLocation());
        assertEquals(organization, punchLog.getOrganization());
    }

    @Test
    public void testPunchLogSetters() {
        PunchLog punchLog = new PunchLog();
        User user = new User();
        Organization organization = new Organization();

        punchLog.setId(1L);
        punchLog.setUser(user);
        punchLog.setTimestamp(LocalDateTime.now());
        punchLog.setType(PunchTypeEnum.EXIT);
        punchLog.setLocation("Home Office");
        punchLog.setOrganization(organization);

        assertNotNull(punchLog.getTimestamp());
        assertEquals(PunchTypeEnum.EXIT, punchLog.getType());
        assertEquals("Home Office", punchLog.getLocation());
    }
}

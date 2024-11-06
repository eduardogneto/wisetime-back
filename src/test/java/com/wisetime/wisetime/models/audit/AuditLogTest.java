package com.wisetime.wisetime.models.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import com.wisetime.wisetime.models.user.User;

import org.junit.jupiter.api.Test;

public class AuditLogTest {

    @Test
    public void testNoArgsConstructor() {
        AuditLog auditLog = new AuditLog();
        assertNotNull(auditLog);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User();
        user.setId(1L);
        LocalDateTime now = LocalDateTime.now();
        AuditLog auditLog = new AuditLog(1L, "LOGIN", user, "User logged in", now);

        assertEquals(1L, auditLog.getId());
        assertEquals("LOGIN", auditLog.getAction());
        assertEquals(user, auditLog.getUser());
        assertEquals("User logged in", auditLog.getDetails());
        assertEquals(now, auditLog.getDate());
    }

    @Test
    public void testCustomConstructor() {
        User user = new User();
        user.setId(2L);
        AuditLog auditLog = new AuditLog("LOGOUT", user, "User logged out");

        assertNull(auditLog.getId());
        assertEquals("LOGOUT", auditLog.getAction());
        assertEquals(user, auditLog.getUser());
        assertEquals("User logged out", auditLog.getDetails());
        assertNull(auditLog.getDate());
    }

    @Test
    public void testBuilder() {
        User user = new User();
        user.setId(3L);
        LocalDateTime now = LocalDateTime.now();

        AuditLog auditLog = AuditLog.builder()
                .id(3L)
                .action("UPDATE_PROFILE")
                .user(user)
                .details("User updated profile information")
                .date(now)
                .build();

        assertEquals(3L, auditLog.getId());
        assertEquals("UPDATE_PROFILE", auditLog.getAction());
        assertEquals(user, auditLog.getUser());
        assertEquals("User updated profile information", auditLog.getDetails());
        assertEquals(now, auditLog.getDate());
    }

    @Test
    public void testSettersAndGetters() {
        AuditLog auditLog = new AuditLog();
        User user = new User();
        user.setId(4L);
        LocalDateTime now = LocalDateTime.now();

        auditLog.setId(4L);
        auditLog.setAction("DELETE_ACCOUNT");
        auditLog.setUser(user);
        auditLog.setDetails("User deleted account");
        auditLog.setDate(now);

        assertEquals(4L, auditLog.getId());
        assertEquals("DELETE_ACCOUNT", auditLog.getAction());
        assertEquals(user, auditLog.getUser());
        assertEquals("User deleted account", auditLog.getDetails());
        assertEquals(now, auditLog.getDate());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(5L);

        User user2 = new User();
        user2.setId(6L);

        LocalDateTime now = LocalDateTime.now();

        AuditLog auditLog1 = new AuditLog(5L, "ACTION", user1, "Details", now);
        AuditLog auditLog2 = new AuditLog(5L, "ACTION", user1, "Details", now);
        AuditLog auditLog3 = new AuditLog(6L, "ACTION", user2, "Details", now);

        assertEquals(auditLog1, auditLog2);
        assertNotEquals(auditLog1, auditLog3);

        assertEquals(auditLog1.hashCode(), auditLog2.hashCode());
        assertNotEquals(auditLog1.hashCode(), auditLog3.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(7L);
        user.setName("Test User");

        LocalDateTime now = LocalDateTime.now();

        String expected = "AuditLog(id=7, action=LOGIN, user=User(id=7, name=Test User), details=User logged in, date=" + now + ")";
        assertEquals(expected, expected);
    }
}

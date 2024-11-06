package com.wisetime.wisetime.models.punch;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class TemporaryPunchTest {

    @Test
    public void testTemporaryPunchCreation() {
        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(1L);

        LocalDateTime timestamp = LocalDateTime.now();

        TemporaryPunch tempPunch = TemporaryPunch.builder()
                .id(1L)
                .user(user)
                .status(PunchTypeEnum.ENTRY)
                .timestamp(timestamp)
                .request(request)
                .type(PunchTypeEnum.EXIT)
                .build();

        assertEquals(1L, tempPunch.getId());
        assertEquals(user, tempPunch.getUser());
        assertEquals(PunchTypeEnum.ENTRY, tempPunch.getStatus());
        assertEquals(timestamp, tempPunch.getTimestamp());
        assertEquals(request, tempPunch.getRequest());
        assertEquals(PunchTypeEnum.EXIT, tempPunch.getType());
    }

    @Test
    public void testTemporaryPunchSetters() {
        TemporaryPunch tempPunch = new TemporaryPunch();
        User user = new User();
        Request request = new Request();

        tempPunch.setId(2L);
        tempPunch.setUser(user);
        tempPunch.setStatus(PunchTypeEnum.EXIT);
        tempPunch.setTimestamp(LocalDateTime.now());
        tempPunch.setRequest(request);
        tempPunch.setType(PunchTypeEnum.ENTRY);

        assertEquals(PunchTypeEnum.EXIT, tempPunch.getStatus());
        assertEquals(PunchTypeEnum.ENTRY, tempPunch.getType());
        assertNotNull(tempPunch.getTimestamp());
    }
}

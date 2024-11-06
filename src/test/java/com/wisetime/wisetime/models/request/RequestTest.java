package com.wisetime.wisetime.models.request;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.user.User;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    @Test
    public void testRequestCreation() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");

        TemporaryPunch temporaryPunch = new TemporaryPunch();
        temporaryPunch.setId(1L);

        Certificate certificate = new Certificate();
        certificate.setId(1L);

        Request request = Request.builder()
                .id(1L)
                .user(user)
                .organization(organization)
                .requestType(RequestTypeEnum.ADICAO_DE_PONTO)
                .justification("Justification for request")
                .status(RequestStatusEnum.PENDENTE)
                .temporaryPunches(List.of(temporaryPunch))
                .certificate(certificate)
                .build();

        assertEquals(1L, request.getId());
        assertEquals(user, request.getUser());
        assertEquals(organization, request.getOrganization());
        assertEquals(RequestTypeEnum.ADICAO_DE_PONTO, request.getRequestType());
        assertEquals("Justification for request", request.getJustification());
        assertEquals(RequestStatusEnum.PENDENTE, request.getStatus());
        assertEquals(1, request.getTemporaryPunches().size());
        assertEquals(certificate, request.getCertificate());
    }

    @Test
    public void testRequestSetters() {
        Request request = new Request();
        User user = new User();
        Organization organization = new Organization();
        TemporaryPunch temporaryPunch = new TemporaryPunch();
        Certificate certificate = new Certificate();

        request.setId(2L);
        request.setUser(user);
        request.setOrganization(organization);
        request.setRequestType(RequestTypeEnum.ATESTADO);
        request.setJustification("Medical reason");
        request.setStatus(RequestStatusEnum.APROVADO);
        request.setTemporaryPunches(List.of(temporaryPunch));
        request.setCertificate(certificate);

        assertEquals(2L, request.getId());
        assertEquals(user, request.getUser());
        assertEquals(organization, request.getOrganization());
        assertEquals(RequestTypeEnum.ATESTADO, request.getRequestType());
        assertEquals("Medical reason", request.getJustification());
        assertEquals(RequestStatusEnum.APROVADO, request.getStatus());
        assertEquals(1, request.getTemporaryPunches().size());
        assertEquals(certificate, request.getCertificate());
    }
}

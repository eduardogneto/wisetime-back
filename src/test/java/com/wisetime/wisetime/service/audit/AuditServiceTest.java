package com.wisetime.wisetime.service.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.audit.AuditLogDTO;
import com.wisetime.wisetime.models.audit.AuditLog;
import com.wisetime.wisetime.models.organization.Address;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.TagUserEnum;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.audit.AuditLogRepository;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditService auditService;

    private Organization organization;
    private Team team;
    private User user;
    private AuditLog auditLog1;
    private AuditLog auditLog2;

    @BeforeEach
    public void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Desenvolvimento");
        organization.setTaxId("123456789");
        organization.setEmail("dev@empresa.com");
        organization.setPhone("1234-5678");
        organization.setAddress(Address.builder()
            .street("Rua A")
            .number("100")
            .complement("Sala 101")
            .city("Cidade X")
            .state("Estado Y")
            .zipCode("12345-678")
            .build());

        team = new Team();
        team.setId(10L);
        team.setName("Desenvolvimento");
        team.setDescription("equipe DEV");
        team.setOrganization(organization);

        user = new User();
        user.setId(1L);
        user.setName("Evelyn");
        user.setEmail("evelyn@teste.com");
        user.setPassword("H2@GSAEG3");
        user.setTag(TagUserEnum.ADMINISTRADOR);
        user.setTeam(team);

        auditLog1 = new AuditLog();
        auditLog1.setId(100L);
        auditLog1.setAction("Criou Organização");
        auditLog1.setUser(user);
        auditLog1.setDetails("Detalhes da ação 1");
        auditLog1.setDate(LocalDateTime.now().minusDays(1));

        auditLog2 = new AuditLog();
        auditLog2.setId(101L);
        auditLog2.setAction("Atualizou Organização");
        auditLog2.setUser(user);
        auditLog2.setDetails("Detalhes da ação 2");
        auditLog2.setDate(LocalDateTime.now());
    }

    @Test
    public void testGetAuditLogs_Success() {
        Long teamId = team.getId();
        List<AuditLog> mockAuditLogs = Arrays.asList(auditLog1, auditLog2);

        when(auditLogRepository.findByUserTeamId(teamId)).thenReturn(mockAuditLogs);

        List<AuditLogDTO> result = auditService.getAuditLogs(teamId);

        System.out.println("Número de logs retornados: " + result.size());

        assertNotNull(result);
        assertEquals(2, result.size());

        AuditLogDTO dto1 = result.get(0);
        assertEquals(auditLog1.getId(), dto1.getId());
        assertEquals(auditLog1.getAction(), dto1.getAction());
        assertEquals(auditLog1.getUser().getName(), dto1.getName());
        assertEquals(auditLog1.getDetails(), dto1.getDetails());
        assertEquals(auditLog1.getDate(), dto1.getDate());

        AuditLogDTO dto2 = result.get(1);
        assertEquals(auditLog2.getId(), dto2.getId());
        assertEquals(auditLog2.getAction(), dto2.getAction());
        assertEquals(auditLog2.getUser().getName(), dto2.getName());
        assertEquals(auditLog2.getDetails(), dto2.getDetails());
        assertEquals(auditLog2.getDate(), dto2.getDate());

        verify(auditLogRepository, times(1)).findByUserTeamId(teamId);
    }

    @Test
    public void testGetAuditLogs_NoLogs() {
        Long teamId = 20L;
        when(auditLogRepository.findByUserTeamId(teamId)).thenReturn(Arrays.asList());

        List<AuditLogDTO> result = auditService.getAuditLogs(teamId);

        System.out.println("Número de logs retornados: " + result.size());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(auditLogRepository, times(1)).findByUserTeamId(teamId);
    }

    @Test
    public void testLogAction_Success() {
        String action = "Criou Organização";
        String details = "Detalhes da ação";
        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        auditService.logAction(action, user, details);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());

        AuditLog savedAuditLog = captor.getValue();
        assertEquals(action, savedAuditLog.getAction());
        assertEquals(user, savedAuditLog.getUser());
        assertEquals(details, savedAuditLog.getDetails());
        assertNotNull(savedAuditLog.getDate());

        Duration duration = Duration.between(savedAuditLog.getDate(), LocalDateTime.now());
        assertTrue(duration.getSeconds() < 5, "A data deve ser próxima do momento atual");
    }
}

package com.wisetime.wisetime.service.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wisetime.wisetime.DTO.certificate.CertificateDTO;
import com.wisetime.wisetime.DTO.punch.ApprovalRequestDTO;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.DTO.request.RequestFilterDTO;
import com.wisetime.wisetime.DTO.request.RequestResponseDTO;
import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.certificate.CertificateStatusEnum;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.PunchLog;
import com.wisetime.wisetime.models.punch.PunchTypeEnum;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.request.RequestStatusEnum;
import com.wisetime.wisetime.models.request.RequestTypeEnum;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.request.RequestRepository;
import com.wisetime.wisetime.service.audit.AuditService;
import com.wisetime.wisetime.service.certificate.CertificateService;
import com.wisetime.wisetime.service.punch.PunchLogService;
import com.wisetime.wisetime.service.punch.TemporaryPunchService;
import com.wisetime.wisetime.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private TemporaryPunchService temporaryPunchService;

    @Mock
    private PunchLogService punchLogService;

    @Mock
    private CertificateService certificateService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private RequestService requestService;

    private User user;
    private Organization organization;
    private Team team;
    private Request request;
    private RequestDTO requestDTO;
    private ApprovalRequestDTO approvalRequestDTO;
    private Certificate certificate;

    @BeforeEach
    public void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Org Name");

        team = new Team();
        team.setId(1L);
        team.setName("Team Name");
        team.setOrganization(organization);

        user = new User();
        user.setId(1L);
        user.setName("User Name");
        user.setTeam(team);

        request = new Request();
        request.setId(1L);
        request.setUser(user);
        request.setOrganization(organization);
        request.setRequestType(RequestTypeEnum.ADICAO_DE_PONTO);
        request.setJustification("Need to add punch");
        request.setStatus(RequestStatusEnum.PENDENTE);

        requestDTO = new RequestDTO();
        requestDTO.setId(user.getId());
        requestDTO.setJustification("Need to add punch");
        requestDTO.setRequestType("ADICAO_DE_PONTO");
        requestDTO.setPunches(new ArrayList<>());

        approvalRequestDTO = new ApprovalRequestDTO();
        approvalRequestDTO.setUserId(1L);
        approvalRequestDTO.setStatus(RequestStatusEnum.APROVADO);

        certificate = new Certificate();
        certificate.setId(1L);
        certificate.setUser(user);
        certificate.setStartDate(LocalDate.now().minusDays(5));
        certificate.setEndDate(LocalDate.now());
        certificate.setJustification("Medical leave");
        certificate.setStatus(CertificateStatusEnum.PENDENTE);
    }

    @Test
    public void testSave() {
        when(requestRepository.save(request)).thenReturn(request);
        Request result = requestService.save(request);
        assertNotNull(result);
        assertEquals(request, result);
        verify(requestRepository).save(request);
    }

    @Test
    public void testFindById_Found() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        Optional<Request> result = requestService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(request, result.get());
        verify(requestRepository).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Request> result = requestService.findById(1L);
        assertFalse(result.isPresent());
        verify(requestRepository).findById(1L);
    }

    @Test
    public void testFindByOrganizationId() {
        when(requestRepository.findByOrganizationId(organization.getId())).thenReturn(Arrays.asList(request));
        List<Request> result = requestService.findByOrganizationId(organization.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository).findByOrganizationId(organization.getId());
    }

    @Test
    public void testCountByTeamIdAndStatus() {
        when(requestRepository.countByTeamIdAndStatus(team.getId(), RequestStatusEnum.PENDENTE)).thenReturn(5L);
        Long count = requestService.countByTeamIdAndStatus(team.getId(), RequestStatusEnum.PENDENTE);
        assertEquals(5L, count);
        verify(requestRepository).countByTeamIdAndStatus(team.getId(), RequestStatusEnum.PENDENTE);
    }

    @Test
    public void testCreate_AdditionOfPunch() {
        when(userService.findEntityById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        Request result = requestService.create(requestDTO);
        assertNotNull(result);
        verify(requestRepository).save(any(Request.class));
        verify(temporaryPunchService).saveAll(anyList());
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testCreate_Certificate() {
        requestDTO.setRequestType("ATESTADO");
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setStartDate(LocalDate.now().minusDays(5).toString());
        certificateDTO.setEndDate(LocalDate.now().toString());
        certificateDTO.setImageBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA");
        requestDTO.setCertificate(certificateDTO);

        when(userService.findEntityById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = requestService.create(requestDTO);
        assertNotNull(result);
        verify(requestRepository).save(any(Request.class));
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testApproveRequest_ApproveAdditionOfPunch() {
        TemporaryPunch tempPunch = new TemporaryPunch();
        tempPunch.setTimestamp(LocalDateTime.now());
        tempPunch.setType(PunchTypeEnum.ENTRY);
        tempPunch.setUser(user);
        tempPunch.setRequest(request);
        request.setTemporaryPunches(Arrays.asList(tempPunch));

        when(userService.findEntityById(approvalRequestDTO.getUserId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(punchLogService.save(any(PunchLog.class))).thenReturn(new PunchLog());
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = requestService.approveRequest(request.getId(), approvalRequestDTO);
        assertNotNull(result);
        assertEquals(RequestStatusEnum.APROVADO, result.getStatus());
        verify(punchLogService).save(any(PunchLog.class));
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testApproveRequest_ApproveCertificate() {
        request.setRequestType(RequestTypeEnum.ATESTADO);
        request.setCertificate(certificate);

        when(userService.findEntityById(approvalRequestDTO.getUserId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(certificateService.save(any(Certificate.class))).thenReturn(certificate);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = requestService.approveRequest(request.getId(), approvalRequestDTO);
        assertNotNull(result);
        assertEquals(RequestStatusEnum.APROVADO, result.getStatus());
        assertEquals(CertificateStatusEnum.APROVADO, certificate.getStatus());
        verify(certificateService).save(any(Certificate.class));
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testApproveRequest_RejectCertificate() {
        approvalRequestDTO.setStatus(RequestStatusEnum.REPROVADO);
        request.setRequestType(RequestTypeEnum.ATESTADO);
        request.setCertificate(certificate);

        when(userService.findEntityById(approvalRequestDTO.getUserId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(certificateService.save(any(Certificate.class))).thenReturn(certificate);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = requestService.approveRequest(request.getId(), approvalRequestDTO);
        assertNotNull(result);
        assertEquals(RequestStatusEnum.REPROVADO, result.getStatus());
        assertEquals(CertificateStatusEnum.REPROVADO, certificate.getStatus());
        verify(certificateService).save(any(Certificate.class));
        verify(auditService).logAction(anyString(), eq(user), anyString());
    }

    @Test
    public void testCountRequestsByStatus() {
        when(requestRepository.countByTeamIdAndStatus(team.getId(), RequestStatusEnum.PENDENTE)).thenReturn(3L);
        when(requestRepository.countByTeamIdAndStatus(team.getId(), RequestStatusEnum.APROVADO)).thenReturn(5L);
        when(requestRepository.countByTeamIdAndStatus(team.getId(), RequestStatusEnum.REPROVADO)).thenReturn(2L);

        Map<String, Long> result = requestService.countRequestsByStatus(team.getId());
        assertNotNull(result);
        assertEquals(3L, result.get("pendente"));
        assertEquals(5L, result.get("aprovado"));
        assertEquals(2L, result.get("reprovado"));
    }

    @Test
    public void testGetFilteredRequests() {
        RequestFilterDTO filterDTO = new RequestFilterDTO();
        filterDTO.setTeamId(team.getId());
        filterDTO.setTypes(Arrays.asList("ADICAO_DE_PONTO"));
        filterDTO.setStatuses(Arrays.asList("PENDENTE"));

        when(requestRepository.findByUserTeamIdAndRequestTypeInAndStatusIn(
                eq(team.getId()),
                anyList(),
                anyList())).thenReturn(Arrays.asList(request));

        List<RequestDTO> result = requestService.getFilteredRequests(filterDTO);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository).findByUserTeamIdAndRequestTypeInAndStatusIn(
                eq(team.getId()),
                anyList(),
                anyList());
    }

    @Test
    public void testMapToFilterDTO() {
        RequestDTO dto = requestService.mapToFilterDTO(request);
        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getJustification(), dto.getJustification());
        assertEquals(request.getStatus().name(), dto.getStatus());
        assertEquals(request.getRequestType().name(), dto.getRequestType());
    }
}

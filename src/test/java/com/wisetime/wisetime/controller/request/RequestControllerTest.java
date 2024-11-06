package com.wisetime.wisetime.controller.request;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetime.wisetime.DTO.request.RequestDTO;
import com.wisetime.wisetime.DTO.request.RequestFilterDTO;
import com.wisetime.wisetime.DTO.request.RequestResponseDTO;
import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.service.request.RequestService;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateRequest_Success() throws Exception {
        RequestDTO requestDTO = new RequestDTO();
        Request savedRequest = new Request();
        RequestResponseDTO responseDTO = new RequestResponseDTO();

        when(requestService.create(any(RequestDTO.class))).thenReturn(savedRequest);
        when(requestService.mapToDTO(savedRequest)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/request/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

        verify(requestService, times(1)).create(any(RequestDTO.class));
        verify(requestService, times(1)).mapToDTO(savedRequest);
    }

    @Test
    public void testGetRequests_Success() throws Exception {
        Long userId = 1L;
        List<RequestDTO> requestDTOs = List.of(new RequestDTO());

        when(requestService.getRequestsByUserId(userId)).thenReturn(requestDTOs);

        mockMvc.perform(get("/api/request/list/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDTOs)));

        verify(requestService, times(1)).getRequestsByUserId(userId);
    }

    @Test
    public void testCountRequestsByStatus_Success() throws Exception {
        Long organizationId = 1L;
        Map<String, Long> statusCounts = Map.of("APROVADO", 5L, "PENDENTE", 10L);

        when(requestService.countRequestsByStatus(organizationId)).thenReturn(statusCounts);

        mockMvc.perform(get("/api/request/countByStatus/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statusCounts)));

        verify(requestService, times(1)).countRequestsByStatus(organizationId);
    }

    @Test
    public void testFilterRequests_Success() throws Exception {
        RequestFilterDTO filterDTO = new RequestFilterDTO();
        List<RequestDTO> filteredRequests = List.of(new RequestDTO());

        when(requestService.getFilteredRequests(any(RequestFilterDTO.class))).thenReturn(filteredRequests);

        mockMvc.perform(post("/api/request/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(filteredRequests)));

        verify(requestService, times(1)).getFilteredRequests(any(RequestFilterDTO.class));
    }

    @Test
    public void testFilterRequests_NoResults() throws Exception {
        RequestFilterDTO filterDTO = new RequestFilterDTO();

        when(requestService.getFilteredRequests(any(RequestFilterDTO.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/request/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(requestService, times(1)).getFilteredRequests(any(RequestFilterDTO.class));
    }
}

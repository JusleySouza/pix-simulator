package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.mapper.PspMapper;
import br.com.pix.simulator.psp.service.PspService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PspController.class)
public class PspControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PspService service;

    @MockitoBean
    private PspMapper mapper;

    @Test
    @DisplayName("You should successfully create an psp and receive a status of 201 Created.")
    void createPsp_WhenValidRequest_ShouldReturnCreated() throws Exception {

        UUID pspId = UUID.randomUUID();
        PspCreateRequest requestDto = new PspCreateRequest(
                "Inter", "001");

        PspResponse responseDto = new PspResponse(
                pspId, "Inter", "001");

        when(service.createPsp(any(PspCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).createPsp(any(PspCreateRequest.class));
    }

}

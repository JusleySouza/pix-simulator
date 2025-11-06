package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.exception.ValidationException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("Creating a PSP with an empty bank name should fail and return a 400 Bad Request status.")
    void createPsp_WhenBankNameIsEmpty_ShouldReturnBadRequest() throws Exception {

        PspCreateRequest invalidRequestDto = new PspCreateRequest(" ", "001");

        when(service.createPsp(invalidRequestDto))
                .thenThrow(new ValidationException("Bank name is required."));

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createPsp(any());
    }

    @Test
    @DisplayName("Creating a PSP with an empty bank code should fail and return a 400 Bad Request status.")
    void createPsp_WhenBankCodeIsEmpty_ShouldReturnBadRequest() throws Exception {

        PspCreateRequest invalidRequestDto = new PspCreateRequest("Inter", " ");

        when(service.createPsp(invalidRequestDto))
                .thenThrow(new ValidationException("Bank code is required."));

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createPsp(any());
    }

    @Test
    @DisplayName("You should be able to successfully query your PSP and receive a 200 OK status.")
    void searchingPspById_WhenPspExists_ShouldReturnOk() throws Exception {
        UUID pspId = UUID.randomUUID();
        PspResponse responseDto = new PspResponse(pspId, "Inter", "001");

        when(service.searchPspById(pspId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/psps/{pspId}", pspId))
                .andExpect(status().isOk());

        verify(service, times(1)).searchPspById(pspId);
    }


    @Test
    @DisplayName("It may fail to check for a non-existent psp and return a 404 Not Found error.")
    void searchingPspById_WhenPspNotFound_ShouldNotFound() throws Exception {
        UUID pspId = UUID.randomUUID();

        when(service.searchPspById(pspId))
                .thenThrow(new ResourceNotFoundException("Psp not found: " + pspId));

        mockMvc.perform(get("/api/v1/psps/{pspId}", pspId))
                .andExpect(status().isNotFound());

        verify(service, times(1)).searchPspById(pspId);
    }

}

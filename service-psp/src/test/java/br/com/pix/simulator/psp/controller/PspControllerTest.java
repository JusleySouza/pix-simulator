package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
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
import static org.hamcrest.Matchers.hasItem;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("bankName"))
                .andExpect(jsonPath("$.errors[0].message").value("Bank name is required."))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createPsp(any());
    }

    @Test
    @DisplayName("Creating a PSP with an empty bank code should fail and return a 400 Bad Request status.")
    void createPsp_WhenBankCodeIsEmpty_ShouldReturnBadRequest() throws Exception {

        PspCreateRequest invalidRequestDto = new PspCreateRequest("Inter", " ");

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'bankCode')].message")
                        .value(hasItem("Bank code is required.")))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createPsp(any());
    }

    @Test
    @DisplayName("Creating a PSP with an shorter bank code should fail and return a 400 Bad Request status.")
    void createPsp_WhenBankCodeIsShorter_ShouldReturnBadRequest() throws Exception {

        PspCreateRequest invalidRequestDto = new PspCreateRequest("Inter", "01");

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("bankCode"))
                .andExpect(jsonPath("$.errors[0].message").value("The bank code must contain 3 digits."))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createPsp(any());
    }

    @Test
    @DisplayName("Creating a PSP with an larger bank code should fail and return a 400 Bad Request status.")
    void createPsp_WhenBankCodeIsLarger_ShouldReturnBadRequest() throws Exception {

        PspCreateRequest invalidRequestDto = new PspCreateRequest("Inter", "00123");

        mockMvc.perform(post("/api/v1/psps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("bankCode"))
                .andExpect(jsonPath("$.errors[0].message").value("The bank code must contain 3 digits."))
                .andExpect(jsonPath("$.message").value("Validation Error"));

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

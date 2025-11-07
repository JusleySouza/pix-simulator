package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.PspMapper;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.repository.PspRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PspServiceTest {

    @Mock
    private Psp psp;
    @Mock
    private PspMapper mapper;
    @Mock
    private PspRepository pspRepository;

    @InjectMocks
    private PspService pspService;

    @Test
    @DisplayName("You should be able to successfully create a PSP when the bank name and code are correct.")
    void createPsp_shouldSucceed_whenDataIsValid() {
        PspCreateRequest request = new PspCreateRequest("Inter", "001");

        when(pspRepository.findByBankCode("001")).thenReturn(Optional.empty());

        when(mapper.toEntity(request)).thenReturn(psp);

        PspResponse expectedResponse = new PspResponse(UUID.randomUUID(), "Inter", "001");
        when(mapper.toResponse(psp)).thenReturn(expectedResponse);

        when(pspRepository.save(psp)).thenReturn(psp);

        PspResponse result = pspService.createPsp(request);

        assertNotNull(result);
        assertEquals(expectedResponse.pspId(), result.pspId());

        verify(mapper, times(1)).toEntity(request);
        verify(mapper, times(1)).toResponse(psp);
    }

    @Test
    @DisplayName("The createPsp command should throw an IllegalArgumentException if the database code already exists.")
    void createPsp_shouldThrowIllegalArgumentException_whenBankCodeExisting() {
        PspCreateRequest request = new PspCreateRequest("Inter", "001");

        when(pspRepository.findByBankCode("001")).thenReturn(Optional.of(psp));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pspService.createPsp(request)
        );

        assertTrue(exception.getMessage().contains("There is already a PSP with the bank code: " + request.bankCode()));

        verify(pspRepository, never()).save(any());
    }

    @Test
    @DisplayName("The system should return the bank's data when the bank ID exists.")
    void searchPspById_shouldSucceed_whenPspExists() {
        UUID pspId = UUID.randomUUID();
        PspResponse expectedResponse = new PspResponse(pspId, "Inter", "001");

        when(pspRepository.findById(pspId)).thenReturn(Optional.of(psp));
        when(mapper.toResponse(psp)).thenReturn(expectedResponse);

        PspResponse result = pspService.searchPspById(pspId);

        assertNotNull(result);
        assertEquals(expectedResponse.pspId(), result.pspId());
        assertEquals(expectedResponse.bankName(), result.bankName());
        assertEquals(expectedResponse.bankCode(), result.bankCode());
        verify(pspRepository, times(1)).findById(pspId);
        verify(mapper, times(1)).toResponse(psp);
    }

    @Test
    @DisplayName("The searchPspById command should throw an ResourceNotFoundException if the id psp is not found.")
    void searchPspById_shouldResourceNotFoundException_whenBankCodeExisting() {
        UUID pspId = UUID.randomUUID();

        when(pspRepository.findById(pspId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pspService.searchPspById(pspId)
        );

        assertTrue(exception.getMessage().contains("PSP not found with ID: " + pspId));
    }

}

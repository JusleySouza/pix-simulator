package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

}

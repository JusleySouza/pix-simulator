package br.com.pix.simulator.dict.dto;

import java.util.UUID;

public record PspAccountValidationResponse(
        UUID accountId,
        UUID userId,
        UUID pspId,
        String userName,
        String pspName
) { }

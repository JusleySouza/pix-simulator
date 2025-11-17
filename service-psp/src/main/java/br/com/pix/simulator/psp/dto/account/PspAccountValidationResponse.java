package br.com.pix.simulator.psp.dto.account;

import java.util.UUID;

public record PspAccountValidationResponse(
        UUID accountId,
        UUID userId,
        UUID pspId,
        String userName,
        String pspName
) { }

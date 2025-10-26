package br.com.pix.simulator.psp.dto.psps;

import java.util.UUID;

public record PspResponse(
        UUID pspId,
        String bankName,
        String bankCode
) {
}

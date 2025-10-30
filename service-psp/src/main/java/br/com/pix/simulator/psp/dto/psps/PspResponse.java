package br.com.pix.simulator.psp.dto.psps;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "API response for bank queries")
public record PspResponse(

        @Schema(description = "Bank identifier", type = "UUID", example = "7494ef92-cbc1-4ed1-bf9b-f9873f916424")
        UUID pspId,
        @Schema(description = "Bank name", type = "String", example = "Itaú")
        String bankName,
        @Schema(description = "Bank code", type = "String", example = "341")
        String bankCode
) {
}

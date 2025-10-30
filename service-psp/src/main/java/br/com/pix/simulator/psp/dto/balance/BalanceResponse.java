package br.com.pix.simulator.psp.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "API response for balance queries")
public record BalanceResponse(

        @Schema(description = "Account identifier", type = "UUID", example = "7494ef92-cbc1-4ed1-bf9b-f9873f916424")
        UUID accountId,
        @Schema(description = "Balance", type = "BigDecimal", example = "150.00")
        BigDecimal balance
) {
}

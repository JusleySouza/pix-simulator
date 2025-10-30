package br.com.pix.simulator.psp.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "API response for account queries")
public record AccountResponse(

        @Schema(description = "Account identifier", type = "UUID", example = "7494ef92-cbc1-4ed1-bf9b-f9873f916424")
        UUID accountId,
        @Schema(description = "Bank name", type = "String", example = "Itaú")
        String pspName,
        @Schema(description = "User name", type = "String", example = "Thiago Azevedo")
        String userName,
        @Schema(description = "Agency number", type = "String", example = "0001")
        String agency,
        @Schema(description = "Account number", type = "String", example = "01234567")
        String accountNumber,
        @Schema(description = "Balance", type = "BigDecimal", example = "150.00")
        BigDecimal balance
) {
}

package br.com.pix.simulator.psp.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request to create a bank account")
public record AccountCreateRequest(

        @NotNull(message = "{psp.not.null}")
        @Schema(description = "Bank identifier", type = "UUID", example = "7494ef92-cbc1-4ed1-bf9b-f9873f916424")
        UUID pspId,

        @NotNull(message = "{user.not.null}")
        @Schema(description = "User identifier", type = "UUID", example = "7494ef92-cbc1-4ed1-bf9b-f9873f916425")
        UUID userId,

        @NotBlank(message = "{agency.not.blank}")
        @Size(min = 4, max = 4, message = "{agency.size}")
        @Schema(description = "Agency number", type = "String", example = "0001")
        String agency,

        @NotBlank(message = "{account.number.not.blank}")
        @Size(min = 8, max = 8, message = "{account.number.size}")
        @Schema(description = "Account number", type = "String", example = "01234567")
        String accountNumber,

        @NotNull(message = "{initial.balance.not.null}")
        @Positive(message = "{initial.balance.positive}")
        @Schema(description = "Initial balance", type = "BigDecimal", example = "150.00")
        BigDecimal initialBalance
) {
}

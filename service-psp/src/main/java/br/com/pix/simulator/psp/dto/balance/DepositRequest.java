package br.com.pix.simulator.psp.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request to create a deposit into a bank account")
public record DepositRequest(

        @NotNull(message = "{value.not.null}")
        @Positive(message = "{value.positive}")
        @Schema(description = "Deposit value", type = "BigDecimal", example = "100.00")
        BigDecimal value
) {
}

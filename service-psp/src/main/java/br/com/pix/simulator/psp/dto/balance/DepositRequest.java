package br.com.pix.simulator.psp.dto.balance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull(message = "{value.not.null}")
        @Positive(message = "{value.positive}")
        BigDecimal value
) {
}

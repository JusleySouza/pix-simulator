package br.com.pix.simulator.psp.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountCreateRequest(
        @NotNull(message = "{psp.not.null}")
        UUID pspId,

        @NotNull(message = "{user.not.null}")
        UUID userId,

        @NotBlank(message = "{agency.not.blank}")
        @Size(min = 4, max = 4, message = "{agency.size}")
        String agency,

        @NotBlank(message = "{account.number.not.blank}")
        @Size(min = 8, max = 8, message = "{account.number.size}")
        String accountNumber,

        @NotNull(message = "{initial.balance.not.null}")
        @Positive(message = "{initial.balance.positive}")
        BigDecimal initialBalance
) {
}

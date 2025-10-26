package br.com.pix.simulator.psp.dto.balance;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponse(
        UUID accountId,
        BigDecimal balance
) {
}

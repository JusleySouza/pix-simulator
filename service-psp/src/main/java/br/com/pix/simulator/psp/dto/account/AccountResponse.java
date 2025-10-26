package br.com.pix.simulator.psp.dto.account;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID accountId,
        String pspName,
        String userName,
        String agency,
        String accountNumber,
        BigDecimal balance
) {
}

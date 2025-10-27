package br.com.pix.simulator.psp.dto.event;

import java.math.BigDecimal;
import java.util.UUID;

//Event received from service-spi
public record TransactionEventRequest(
        UUID transactionId,
        UUID originAccount,
        UUID destinationAccount,
        BigDecimal value
) { }

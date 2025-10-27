package br.com.pix.simulator.psp.dto.event;

import java.util.UUID;

// Event sent to the service-spi
public record TransactionEventResponse(
        UUID transactionID,
        boolean success,
        String failureReason
) { }

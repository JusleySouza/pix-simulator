package br.com.pix.simulator.dict.dto;

import br.com.pix.simulator.dict.model.enums.AccountType;
import br.com.pix.simulator.dict.model.enums.KeyStatus;
import br.com.pix.simulator.dict.model.enums.OwnerType;
import br.com.pix.simulator.dict.model.enums.PixKeyType;

import java.time.LocalDateTime;
import java.util.UUID;

public record KeyResponse(
        String keyValue,
        PixKeyType keyType,
        UUID accountId,
        UUID pspId,
        KeyStatus keyStatus,
        AccountType accountType,
        OwnerType ownerType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }

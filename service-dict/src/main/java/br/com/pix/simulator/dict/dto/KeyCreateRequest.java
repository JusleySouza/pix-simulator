package br.com.pix.simulator.dict.dto;

import br.com.pix.simulator.dict.model.enums.AccountType;
import br.com.pix.simulator.dict.model.enums.OwnerType;
import br.com.pix.simulator.dict.model.enums.PixKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record KeyCreateRequest(
        @NotNull(message = "{key.type.not.null}")
        PixKeyType keyType,

        @NotBlank(message = "{key.value.not.blank}")
        String keyValue,

        @NotNull(message = "{account.id.not.null}")
        UUID accountId,

        @NotNull(message = "{user.id.not.null}")
        UUID userId,

        @NotNull(message = "{psp.id.not.null}")
        UUID pspId,

        @NotNull(message = "{account.type.not.null}")
        AccountType accountType,

        @NotNull(message = "{owner.type.not.null}")
        OwnerType ownerType
) {}

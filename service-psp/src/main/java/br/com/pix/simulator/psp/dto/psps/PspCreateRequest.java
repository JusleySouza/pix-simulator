package br.com.pix.simulator.psp.dto.psps;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PspCreateRequest(
        @NotBlank(message = "{bank.name.not.blank}")
        String bankName,

        @NotBlank(message = "{bank.code.not.blank}")
        @Size(min = 3, max = 3, message = "{bank.code.size}")
        String bankCode
) {
}

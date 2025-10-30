package br.com.pix.simulator.psp.dto.psps;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a banking service provider (PSP)")
public record PspCreateRequest(

        @NotBlank(message = "{bank.name.not.blank}")
        @Schema(description = "Bank name", type = "String", example = "Itaú")
        String bankName,

        @NotBlank(message = "{bank.code.not.blank}")
        @Size(min = 3, max = 3, message = "{bank.code.size}")
        @Schema(description = "Bank code", type = "String", example = "341")
        String bankCode
) {
}

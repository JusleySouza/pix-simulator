package br.com.pix.simulator.psp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Request to create a user")
public record UserCreateRequest(

        @NotBlank(message = "{user.name.not.blank}")
        @Schema(description = "User name", type = "String", example = "Pablo Silva")
        String name,

        @NotBlank(message = "{cpf.not.blank}")
        @CPF(message = "{user.cpf.invalid}")
        @Schema(description = "User CPF", type = "String", example = "385.049.820-41")
        String cpf
) {
}

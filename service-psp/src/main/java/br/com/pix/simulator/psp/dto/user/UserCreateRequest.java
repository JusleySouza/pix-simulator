package br.com.pix.simulator.psp.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserCreateRequest(
        @NotBlank(message = "{user.name.not.blank}")
        String name,

        @NotBlank(message = "{cpf.not.blank}")
        @CPF(message = "{user.cpf.invalid}")
        String cpf
) {
}

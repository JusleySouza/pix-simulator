package br.com.pix.simulator.psp.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String name,
        String cpf
) {
}

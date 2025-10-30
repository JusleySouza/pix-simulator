package br.com.pix.simulator.psp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "API response for user queries")
public record UserResponse(

        @Schema(description = "User identifier", type = "UUID", example = "e7b8f8a1-3c4d-4f5e-9a6b-7c8d9e0f1a2b")
        UUID userId,
        @Schema(description = "User name", type = "String", example = "Pablo Silva")
        String name,
        @Schema(description = "User CPF", type = "String", example = "385.049.820-41")
        String cpf
) {
}

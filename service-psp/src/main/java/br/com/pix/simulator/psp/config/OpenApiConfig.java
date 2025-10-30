package br.com.pix.simulator.psp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import lombok.Generated;

import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Service PSP",
                version = "1.0",
                description = "API for simulating the opening of bank accounts and checking balances.",
                license = @License(name = "MIT")
        )
)
@Configuration
@Generated
public class OpenApiConfig {
}

package com.easymenu.api.configuration;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer customGlobalHeaderOpenApi() {
        return openApi -> {
            openApi.getComponents()
                .addSecuritySchemes("bearer-jwt",
                    new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
            openApi.addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
        };
    }
}

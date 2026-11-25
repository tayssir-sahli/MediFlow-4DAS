package com.mediflow.dossier.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dossierOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("MediFlow - API Dossier Médical")
                .description("Microservice de gestion du dossier médical numérique : allergies, "
                        + "antécédents, consultations et documents d'un patient.")
                .version("v1")
                .contact(new Contact().name("Tayssir")));
    }
}

package com.sas.dhop.site.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenAPI(
            @Value("${group.open.api.docs}") String docs, @Value("${group.package}") String packageToScan) {
        return GroupedOpenApi.builder()
                .group(docs)
                .packagesToScan(packageToScan)
                .build();
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${open-api.title}") String title,
            @Value("${open-api.version}") String version,
            @Value("${open-api.description}") String description,
            @Value("${open-api.server}") String server,
            @Value("${open-api.server-name}") String serverName) {
        return new OpenAPI()
                .servers(List.of(new Server().url(server).description(serverName)))
                .info(new Info().title(title).description(description).version(version))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }
}

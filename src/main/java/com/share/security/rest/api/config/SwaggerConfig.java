package com.share.security.rest.api.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot REST API With Share Jwt Security Documentation",
                description = "Spring Boot REST API With Share Jwt Security Documentation",
                summary = "Spring Boot REST API With Share Jwt Security Documentation",
                termsOfService = "Term & Condition Applied",
                contact = @Contact(
                        name = "Pritam Ray",
                        email = "raypritam49000@gmail.com",
                        url = "www.google.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.javaguides.net/license"
                ),
                version = "v1.0"),
        servers = {
                @Server(description = "local", url = "http://localhost:9090"),
        },
        security = @SecurityRequirement(name = "Bearer Authentication"),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot REST API With Share Jwt Security Documentation",
                url = "https://www.javaguides.net/user_management.html"
        )
)

@SecurityScheme(
        name = "Bearer Authentication",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "Bearer",
        scheme = "Bearer",
        description = "This is JWT Authentication"
)
@Configuration
public class SwaggerConfig {

}
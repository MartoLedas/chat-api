package com.marto.chatapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI chatApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chat Room API")
                        .description("REST API for a simple chat room application with user management")
                        .version("1.0.0"));
    }
}

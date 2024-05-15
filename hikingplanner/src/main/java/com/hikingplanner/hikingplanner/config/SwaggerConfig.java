package com.hikingplanner.hikingplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("하이킹 플래너 API")
                .description("DKU 2024-1 캡스톤 하이킹플래너 API 목록입니다.");

        return new OpenAPI()
                .info(info);
    }
}
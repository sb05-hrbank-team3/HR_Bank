package com.codeit.hrbank.config;

import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${swagger.server-url}")
  private String serverUrl;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("API 문서")
            .version("v1.0.0")
            .description("Swagger API 문서입니다."))
        .servers(List.of(
            new io.swagger.v3.oas.models.servers.Server()
                .url(serverUrl)
                .description("서버")
        ));
  }
}

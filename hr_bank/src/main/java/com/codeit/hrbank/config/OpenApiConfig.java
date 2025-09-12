package com.codeit.hrbank.config;

import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  // 개발 환경에 맞춰서 바꿈/
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("API 문서")
            .version("v1.0.0")
            .description("Swagger API 문서입니다."))
        .servers(List.of(
            new io.swagger.v3.oas.models.servers.Server()
                .url("https://hrbank-production.up.railway.app/")
                .description("프로덕션 서버")
        ));
  }


}

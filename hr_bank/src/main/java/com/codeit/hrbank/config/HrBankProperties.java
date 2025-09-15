package com.codeit.hrbank.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hrbank")
public class HrBankProperties {
  private Cors cors = new Cors();

  @Data
  public static class Cors {
    private List<String> allowedOrigins;
  }
}

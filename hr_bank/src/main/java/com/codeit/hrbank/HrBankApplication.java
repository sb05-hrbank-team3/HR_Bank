package com.codeit.hrbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(HrBankApplication.class, args);
    System.out.println("http://localhost:8080");
    System.out.println("http://localhost:8080/swagger-ui/index.html");
  }

}

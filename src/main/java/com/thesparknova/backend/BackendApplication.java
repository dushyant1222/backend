package com.thesparknova.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.*;
import org.springframework.boot.autoconfigure.jdbc.*;

@SpringBootApplication(
  exclude = {
    R2dbcAutoConfiguration.class,
    R2dbcTransactionManagerAutoConfiguration.class,
  }
)
public class BackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
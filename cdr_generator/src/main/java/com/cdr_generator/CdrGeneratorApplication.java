package com.cdr_generator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API for CDR generator service", version = "1.0", description = "Using this API you can generate CDRs and send them to BRT service"))
public class CdrGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdrGeneratorApplication.class, args);
    }

}

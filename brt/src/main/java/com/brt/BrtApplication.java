package com.brt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API for BRT service", version = "1.0", description = "Using this API, you can send BRT histories to HRS service and receive billed call data in return"))
public class BrtApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrtApplication.class, args);
	}

}

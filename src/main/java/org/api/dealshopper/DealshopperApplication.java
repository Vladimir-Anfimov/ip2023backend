package org.api.dealshopper;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class DealshopperApplication {
	public static void main(String[] args) {
		SpringApplication.run(DealshopperApplication.class, args);
	}
}

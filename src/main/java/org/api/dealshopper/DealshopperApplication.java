package org.api.dealshopper;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@OpenAPIDefinition
public class DealshopperApplication {
	public static void main(String[] args) {
		SpringApplication.run(DealshopperApplication.class, args);
	}
}

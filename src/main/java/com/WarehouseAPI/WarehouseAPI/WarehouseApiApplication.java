package com.WarehouseAPI.WarehouseAPI;

import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@OpenAPIDefinition(
		info = @Info(
				title = "Warehouse Management",
				version = "1.0",
				description = "API for My Application"
		)
)
@SpringBootApplication
@EnableMongoRepositories
public class WarehouseApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseApiApplication.class, args);
	}

}

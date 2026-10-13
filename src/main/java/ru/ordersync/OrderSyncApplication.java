package ru.ordersync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OrderSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSyncApplication.class, args);
	}

}

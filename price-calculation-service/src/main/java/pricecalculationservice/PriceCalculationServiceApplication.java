package pricecalculationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class PriceCalculationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceCalculationServiceApplication.class, args);
	}

}

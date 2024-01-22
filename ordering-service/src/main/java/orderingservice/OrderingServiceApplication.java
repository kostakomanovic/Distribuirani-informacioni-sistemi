package orderingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class OrderingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingServiceApplication.class, args);
	}

}

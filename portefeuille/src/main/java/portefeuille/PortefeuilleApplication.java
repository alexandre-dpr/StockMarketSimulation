package portefeuille;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class PortefeuilleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortefeuilleApplication.class, args);
	}

}

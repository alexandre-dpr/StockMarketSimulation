package bourse;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class BourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BourseApplication.class, args);
	}

}

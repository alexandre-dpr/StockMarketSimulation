package portefeuille;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import portefeuille.service.IScheduledTasksService;

@SpringBootApplication
@EnableRabbit
@EnableScheduling
public class PortefeuilleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PortefeuilleApplication.class, args);
    }

    @Autowired
    private IScheduledTasksService scheduledTasksService;

    @Override
    public void run(String... args) {
        scheduledTasksService.calculateRanks();
    }
}

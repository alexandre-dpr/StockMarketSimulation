package portefeuille;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import portefeuille.service.IScheduledTasksService;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
@EnableRabbit
@EnableScheduling
public class PortefeuilleApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PortefeuilleApplication.class, args);
    }

    @Autowired
    private IScheduledTasksService scheduledTasksService;

    @Override
    public void run(String... args) {
        // Import des fixtures au premier démarrage
        String sql = "SELECT COUNT(*) FROM your_database.portefeuille";
        List<Integer> result = jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getInt(1));

        if (result.get(0) == 0) {
            Resource resource = new ClassPathResource("import.sql");
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
            databasePopulator.execute(dataSource);
        }

        // Calcul des rangs au démarrage
        scheduledTasksService.calculateRanks();
    }
}

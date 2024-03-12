package portefeuille.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasksService {

    @Autowired
    private RankService rankService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Marker SCHEDULED_TASK_MARKER = MarkerFactory.getMarker("SCHEDULED-TASK");

    @Scheduled(cron = "0 0 22 * * *")
    public void calculateRanks() {
        try {
            logger.info(SCHEDULED_TASK_MARKER, "Starting ranks calculation");
            rankService.calculateRanks();
            logger.info(SCHEDULED_TASK_MARKER, "Ranks calculation ended");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

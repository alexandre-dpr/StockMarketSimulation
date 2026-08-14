package portefeuille.service.impl;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import portefeuille.service.IScheduledTasksService;

@Service
@RequiredArgsConstructor
public class ScheduledTasksService implements IScheduledTasksService {

    private final portefeuille.service.IRankService rankService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Marker SCHEDULED_TASK_MARKER = MarkerFactory.getMarker("SCHEDULED-TASK");

    @Override
    @Scheduled(cron = "0 0 22 * * *")
    public void calculateRanks() {
        try {
            logger.info(SCHEDULED_TASK_MARKER, "Starting ranks calculation");
            rankService.calculateRanks();
            logger.info(SCHEDULED_TASK_MARKER, "Ranks calculation ended");

        } catch (Exception e) {
            logger.error(SCHEDULED_TASK_MARKER, "Error during ranks calculation", e);
        }
    }
}

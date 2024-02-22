package bourse.service;

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
    private StockService stockService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Marker SCHEDULED_TASK_MARKER = MarkerFactory.getMarker("SCHEDULED-TASK");

    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    private void getTrends() {
        try {
            logger.info(SCHEDULED_TASK_MARKER, "Fetching trends");
            stockService.getTrends();
            logger.info(SCHEDULED_TASK_MARKER, "Trends successfully fetched");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

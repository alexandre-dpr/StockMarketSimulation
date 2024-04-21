package portefeuille.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface IScheduledTasksService {
    /**
     * Calcule périodiquement les rangs des portefeuilles
     */
    @Scheduled(cron = "0 0 22 * * *")
    void calculateRanks();
}

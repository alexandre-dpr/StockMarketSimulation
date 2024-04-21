package portefeuille.service;

import org.springframework.transaction.annotation.Transactional;
import portefeuille.dto.PerformanceDto;
import portefeuille.modele.PerformanceHistory;

import java.util.List;

public interface IPerformanceHistoryService {
    /**
     * Sauvegarde la performance de l'utilisateur
     *
     * @param performanceDto performance de l'utilisateur
     * @param username       nom de l'utilisateur
     */
    @Transactional
    void savePerformance(PerformanceDto performanceDto, String username);

    /**
     * Récupère l'historique de performance de l'utilisateur
     *
     * @param username nom de l'utilisateur
     * @return historique de performance de l'utilisateur
     */
    List<PerformanceHistory> getPerformanceHistory(String username);
}

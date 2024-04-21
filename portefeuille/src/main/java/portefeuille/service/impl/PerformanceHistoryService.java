package portefeuille.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.config.Constants;
import portefeuille.dto.PerformanceDto;
import portefeuille.modele.PerformanceHistory;
import portefeuille.repository.PerformanceHistoryRepository;
import portefeuille.service.IPerformanceHistoryService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PerformanceHistoryService implements IPerformanceHistoryService {

    @Autowired
    PerformanceHistoryRepository performanceHistoryRepository;

    @Override
    @Transactional
    public void savePerformance(PerformanceDto performanceDto, String username) {
        if (!performanceHistoryRepository.isPerformanceSavedSinceDate(LocalDateTime.now().minusHours(Constants.HISTORY_SAVE_INTERVAL), username)) {
            PerformanceHistory performance = new PerformanceHistory(performanceDto.getPercentage(), performanceDto.getValue(), username);
            performanceHistoryRepository.save(performance);
        }
    }

    @Override
    public List<PerformanceHistory> getPerformanceHistory(String username) {
        return performanceHistoryRepository.findPerformanceHistoryByUsername(username);
    }
}

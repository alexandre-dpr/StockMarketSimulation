package portefeuille.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import portefeuille.config.Constants;
import portefeuille.dto.PerformanceDto;
import portefeuille.modele.PerformanceHistory;
import portefeuille.repository.PerformanceHistoryRepository;
import portefeuille.service.impl.PerformanceHistoryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PerformanceHistoryServiceTest {

    @Mock
    PerformanceHistoryRepository performanceHistoryRepository;

    @InjectMocks
    PerformanceHistoryService service;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void savePerformance_OK() {
        String username = "username";
        PerformanceDto performanceDto = new PerformanceDto("30%", 40.);
        when(performanceHistoryRepository.isPerformanceSavedSinceDate(LocalDateTime.now().minusHours(Constants.HISTORY_SAVE_INTERVAL), username)).thenReturn(false);
        service.savePerformance(performanceDto, username);
        verify(performanceHistoryRepository, times(1)).save(any(PerformanceHistory.class));
    }

    @Test
    public void getPerformanceHistory_OK() {
        String username = "username";
        PerformanceHistory performanceHistory = PerformanceHistory.builder()
                .id(1)
                .value(10.)
                .date(LocalDateTime.now())
                .username(username)
                .percentage("30%")
                .build();
        List<PerformanceHistory> listPerfHistory = new ArrayList<>();
        listPerfHistory.add(performanceHistory);
        when(performanceHistoryRepository.findPerformanceHistoryByUsername(username)).thenReturn(listPerfHistory);
        List<PerformanceHistory> result = service.getPerformanceHistory(username);
        assertEquals(listPerfHistory, result);
    }
}
package bourse.service.impl;

import bourse.modele.Ticker;
import bourse.repository.TickerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickerServiceTest {

    @Mock
    private TickerRepository tickerRepository;

    @InjectMocks
    private TickerService tickerService;

    private Ticker mockTicker;

    @BeforeEach
    void setUp() {
        mockTicker = Ticker.builder()
                .ticker("AAPL")
                .Name("Apple Inc.")
                .Category("Technology")
                .build();
    }

    @Test
    void getTickerOpt_ShouldReturnTicker_WhenTickerExists() {
        // Arrange
        when(tickerRepository.findById("AAPL")).thenReturn(Optional.of(mockTicker));

        // Act
        Optional<Ticker> result = tickerService.getTickerOpt("AAPL");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getTicker());
        assertEquals("Apple Inc.", result.get().getName());
        assertEquals("Technology", result.get().getCategory());
        verify(tickerRepository, times(1)).findById("AAPL");
    }

    @Test
    void getTickerOpt_ShouldReturnEmpty_WhenTickerDoesNotExist() {
        // Arrange
        when(tickerRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        // Act
        Optional<Ticker> result = tickerService.getTickerOpt("UNKNOWN");

        // Assert
        assertTrue(result.isEmpty());
        verify(tickerRepository, times(1)).findById("UNKNOWN");
    }

    @Test
    void saveTicker_ShouldCallRepositorySave() {
        // Act
        tickerService.saveTicker(mockTicker);

        // Assert
        verify(tickerRepository, times(1)).save(mockTicker);
    }
}

package bourse.service;

import bourse.modele.Ticker;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ITickerService {
    Page<Ticker> findTickerByName(String name, int page);

    Optional<Ticker> getTickerOpt(String ticker);

    Page<Ticker> getStocksList(int page);

    @Transactional
    void saveTicker(Ticker ticker);
}

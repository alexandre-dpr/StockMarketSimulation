package bourse.service;

import bourse.modele.Ticker;
import bourse.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TickerService {

    @Autowired
    TickerRepository tickerRepository;

    public Page<Ticker> findTickerByName(String name, int page) {
        if (page >= 1) {
            Pageable pageable = PageRequest.of(page - 1, 50);
            return tickerRepository.findBySimilarName(name, pageable);
        } else {
            throw new IllegalArgumentException("Pagination must be superior to 1");
        }
    }

    public Optional<Ticker> getTickerOpt(String ticker) {
        return tickerRepository.findById(ticker);
    }

    public Page<Ticker> getStocksList(int page) {
        if (page >= 1) {
            Pageable pageable = PageRequest.of(page - 1, 50);
            return tickerRepository.findAll(pageable);
        } else {
            throw new IllegalArgumentException("Pagination must be superior to 1");
        }
    }
}

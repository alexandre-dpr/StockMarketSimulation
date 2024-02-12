package bourse.service;

import bourse.modele.Ticker;
import bourse.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TickerService {

    @Autowired
    TickerRepository tickerRepository;

    public List<Ticker> findTickerByName(String name) {
        return tickerRepository.findBySimilarName(name);
    }

    public Optional<Ticker> getTickerOpt(String ticker) {
        return tickerRepository.findById(ticker);
    }

    public Page<Ticker> getStocksList(int page) {
        Pageable pageable = PageRequest.of(page, 50);
        return tickerRepository.findAll(pageable);
    }
}

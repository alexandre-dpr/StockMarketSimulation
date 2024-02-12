package bourse.service;

import bourse.modele.Ticker;
import bourse.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}

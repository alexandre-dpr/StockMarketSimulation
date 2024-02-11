package bourse.controller;

import bourse.dto.StockDto;
import bourse.enums.Range;
import bourse.modele.Ticker;
import bourse.service.StockService;
import bourse.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bourse")
public class BourseController {

    @Autowired
    StockService stockService;

    @Autowired
    TickerService tickerService;

    @GetMapping("/stock/{ticker}")
    public StockDto bourse(@PathVariable String ticker, @RequestParam Range range) throws IOException {
        return stockService.getStock(ticker, range);
    }

    @PostMapping("/tickerAutocomplete")
    public List<Ticker> findTickerByName(@RequestParam String name) {
        return tickerService.findTickerByName(name);
    }
}

package bourse.controller;

import bourse.dto.StockDto;
import bourse.dto.StockListDto;
import bourse.enums.Range;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Ticker;
import bourse.service.StockService;
import bourse.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public StockDto getStock(@PathVariable String ticker, @RequestParam Range range) throws IOException, UnauthorizedException {
        return stockService.getStock(ticker, range);
    }

    @GetMapping("/stocks/{tickers}")
    public List<StockListDto> getStocks(@RequestParam Range range, @PathVariable String... tickers) throws IOException, UnauthorizedException {
        return stockService.getStocks(tickers, range);
    }

    @PostMapping("/tickerAutocomplete")
    public List<Ticker> findTickerByName(@RequestParam String name) {
        return tickerService.findTickerByName(name);
    }

    @GetMapping("/stocksList")
    public Page<Ticker> getStocksList(@RequestParam Integer page) {
        return tickerService.getStocksList(page);
    }
}

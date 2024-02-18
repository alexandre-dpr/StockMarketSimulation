package bourse.controller;

import bourse.dto.StockDto;
import bourse.dto.StockListDto;
import bourse.enums.Range;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Ticker;
import bourse.rabbitMq.RabbitMqSender;
import bourse.service.StockService;
import bourse.service.TickerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bourse")
public class BourseController {

    @Autowired
    RabbitMqSender sender;
    @Autowired
    StockService stockService;

    @Autowired
    TickerService tickerService;

    @GetMapping("/stock/{ticker}")
    public ResponseEntity<StockDto> getStock(@PathVariable @NotBlank String ticker, @RequestParam @NotBlank Range range) throws IOException, UnauthorizedException {
        return ResponseEntity.ok().body(stockService.getStock(ticker, range));
    }

    @GetMapping("/stocks/{tickers}")
    public ResponseEntity<List<StockListDto>> getStocks(@RequestParam @NotBlank Range range, @PathVariable @NotBlank String... tickers) throws IOException, UnauthorizedException {
        return ResponseEntity.ok().body(stockService.getStocks(tickers, range));
    }

    @PostMapping("/tickerAutocomplete")
    public ResponseEntity<Page<Ticker>> findTickerByName(@RequestParam String name, @RequestParam @NotNull Integer page) {
        return ResponseEntity.ok().body(tickerService.findTickerByName(name, page));
    }

    @GetMapping("/stocksList")
    public ResponseEntity<Page<Ticker>> getStocksList(@RequestParam @NotNull Integer page) {
        return ResponseEntity.ok().body(tickerService.getStocksList(page));
    }
}

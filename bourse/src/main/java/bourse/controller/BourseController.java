package bourse.controller;

import bourse.dto.StockDto;
import bourse.dto.StockTrendListDto;
import bourse.enums.Range;
import bourse.exceptions.NotFoundException;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Ticker;
import bourse.service.StockService;
import bourse.service.TickerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/bourse")
public class BourseController {

    @Autowired
    StockService stockService;

    @Autowired
    TickerService tickerService;

    @GetMapping("/stock/{ticker}")
    public ResponseEntity<StockDto> getStock(@PathVariable @NotBlank String ticker, @RequestParam @NotNull Range range) throws IOException, UnauthorizedException, NotFoundException {
        return ResponseEntity.ok().body(stockService.getStock(ticker, range));
    }

    @PostMapping("/tickerAutocomplete")
    public ResponseEntity<Page<Ticker>> findTickerByName(@RequestParam String name, @RequestParam @NotNull Integer page) {
        return ResponseEntity.ok().body(tickerService.findTickerByName(name, page));
    }

    @GetMapping("/market")
    public ResponseEntity<Page<Ticker>> getStocksList(@RequestParam @NotNull Integer page) {
        return ResponseEntity.ok().body(tickerService.getStocksList(page));
    }

    @GetMapping("/trends")
    public ResponseEntity<StockTrendListDto> getTrends() throws UnauthorizedException, IOException {
        return ResponseEntity.ok().body(stockService.getTrends());
    }
}

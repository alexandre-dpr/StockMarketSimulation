package bourse.service;

import bourse.dto.StockDto;
import bourse.dto.StockListDto;
import bourse.enums.Range;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Stock;
import bourse.modele.Ticker;
import bourse.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    TickerService tickerService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${stocks.fetch.interval}")
    private Integer STOCKS_FETCH_INTERVAL;

    @Value("${api.token.tiingo}")
    private String TIINGO_API_TOKEN;

    /**
     * Récupère l'historique de prix d'une action pour un interval donné. Si l'action n'a jamais été récupérée, récupère aussi les informations dessus.
     *
     * @param ticker Ticker de l'action à récupérer
     * @param range  1d, 1y
     * @return StockDto
     */
    public StockDto getStock(String ticker, Range range) throws IOException, UnauthorizedException {
        // On vérifie si on n'a pas déjà fetch les infos très récemment
        Optional<Stock> optStock = getSavedStock(ticker);
        Stock stock = null;

        if (optStock.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            stock = optStock.get();

            switch (range) {
                case ONE_DAY -> {
                    if (Objects.nonNull(stock.getLastOneDayUpdate()) && Duration.between(stock.getLastOneDayUpdate(), now).toMinutes() < STOCKS_FETCH_INTERVAL) {
                        return StockDto.oneDayDto(stock);
                    }
                }
                case ONE_YEAR -> {
                    if (Objects.nonNull(stock.getLastOneYearUpdate()) && Duration.between(stock.getLastOneYearUpdate(), now).toMinutes() < STOCKS_FETCH_INTERVAL) {
                        return StockDto.oneYearDto(stock);
                    }
                }
            }
        }

        String YAHOO_API = "https://query1.finance.yahoo.com/v8/finance/chart/%s?region=US&lang=en-US&includePrePost=false&interval=%s&useYfid=true&range=%s&corsDomain=finance.yahoo.com&.tsrc=finance";

        switch (range) {
            case ONE_DAY -> YAHOO_API = String.format(YAHOO_API, ticker, "2m", range.label);
            case ONE_YEAR -> YAHOO_API = String.format(YAHOO_API, ticker, "1d", range.label);
            default -> throw new IllegalArgumentException("Invalid range");
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(YAHOO_API);

        try {
            logger.debug("Calling Yahoo Finance API");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                JsonNode metaData = jsonNode
                        .path("chart")
                        .path("result")
                        .path(0)
                        .path("meta");

                // Dans le cas où l'action n'a jamais été enregistrée
                if (optStock.isEmpty()) {
                    Optional<JsonNode> stockDescOpt = getStockDescription(ticker);

                    if (stockDescOpt.isPresent()) {

                        JsonNode stockDesc = stockDescOpt.get();

                        stock = Stock.builder()
                                .ticker(ticker)
                                .name(stockDesc.path("name").asText())
                                .exchangeCode(stockDesc.path("exchangeCode").asText())
                                .description(stockDesc.path("description").asText())
                                .currency(metaData.path("currency").asText())
                                .build();
                    } else {

                        stock = Stock.builder()
                                .ticker(ticker)
                                .currency(metaData.path("currency").asText())
                                .build();
                    }

                    Optional<Ticker> tickerOpt = tickerService.getTickerOpt(ticker);
                    if (tickerOpt.isPresent()) {
                        Ticker t = tickerOpt.get();
                        stock.setCategory(t.getCategory());

                        if (Objects.isNull(stock.getName())) {
                            stock.setName(t.getName());
                        }
                    }

                }

                stock.setPrice(metaData.path("regularMarketPrice").asDouble());

                switch (range) {
                    case ONE_DAY -> {
                        stock.setOneDayHistory(responseBody);
                        stock.setLastOneDayUpdate(LocalDateTime.now());
                        saveStock(stock);
                        return StockDto.oneDayDto(stock);
                    }
                    case ONE_YEAR -> {
                        stock.setOneYearHistory(responseBody);
                        stock.setLastOneYearUpdate(LocalDateTime.now());
                        saveStock(stock);
                        return StockDto.oneYearDto(stock);
                    }
                    default -> throw new IllegalArgumentException("Invalid range");
                }

            } else {
                throw new UnauthorizedException(String.format("Failed to get stock price for ticker '%s'", ticker));
            }
        } catch (IOException e) {
            throw new IOException(String.format("Failed to get stock price for ticker '%s'", ticker));
        }
    }

    /**
     * Appelle l'API Tiingo pour récupérer les informations sur l'action
     */
    private Optional<JsonNode> getStockDescription(String ticker) throws IOException, UnauthorizedException {
        String TIINGO_API = String.format("https://api.tiingo.com/tiingo/daily/%s?token=%s", ticker, TIINGO_API_TOKEN);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(TIINGO_API);

        try {
            logger.debug("Calling Tiingo API");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                return Optional.of(objectMapper.readTree(responseBody));

            } else if (response.getStatusLine().getStatusCode() == 404) {
                return Optional.empty();

            } else {
                throw new UnauthorizedException(String.format("Failed to get stock price for ticker '%s'", ticker));
            }

        } catch (IOException e) {
            throw new IOException(String.format("Failed to get stock price for ticker '%s'", ticker));
        }
    }

    public List<StockListDto> getStocks(String[] tickers, Range range) throws IOException, UnauthorizedException {
        List<StockListDto> stocks = new ArrayList<>();
        for (String ticker : tickers) {
            stocks.add(
                    new StockListDto(
                            ticker,
                            getStock(ticker, range)
                    )
            );
        }
        return stocks;
    }

    private Optional<Stock> getSavedStock(String ticker) {
        return stockRepository.findById(ticker);
    }

    @Transactional
    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }
}

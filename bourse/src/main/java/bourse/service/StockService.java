package bourse.service;

import bourse.dto.StockDto;
import bourse.dto.StockListDto;
import bourse.dto.StockTrendDto;
import bourse.dto.StockTrendListDto;
import bourse.enums.Range;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Stock;
import bourse.modele.StockTrendList;
import bourse.modele.Ticker;
import bourse.repository.StockRepository;
import bourse.repository.StockTrendListRepository;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockTrendListRepository stockTrendListRepository;

    @Autowired
    private TickerService tickerService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${fetch.interval.stocks}")
    private Integer STOCKS_FETCH_INTERVAL;

    @Value("${fetch.interval.trends}")
    private Integer TRENDS_FETCH_INTERVAL;

    @Value("${api.token.fmi}")
    private String FMI_API_TOKEN;

    @Value("${api.token.alphavantage}")
    private String ALPHAVANTAGE_API_TOKEN;

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
                                .name(stockDesc.path("companyName").asText())
                                .exchange(stockDesc.path("exchange").asText())
                                .exchangeShortName(stockDesc.path("exchangeShortName").asText())
                                .category(stockDesc.path("sector").asText())
                                .description(stockDesc.path("description").asText())
                                .marketCap(stockDesc.path("mktCap").asInt())
                                .currency(metaData.path("currency").asText())
                                .website(stockDesc.path("website").asText())
                                .ceo(stockDesc.path("ceo").asText())
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

                        if (Objects.nonNull(t.getCategory())) {
                            stock.setCategory(t.getCategory());
                        }

                        if (Objects.isNull(stock.getName())) {
                            stock.setName(t.getName());
                        }
                    } else {
                        Ticker newTicker = Ticker.builder()
                                .ticker(ticker)
                                .Name(stock.getName())
                                .Category(stock.getCategory())
                                .build();
                        tickerService.saveTicker(newTicker);
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
     * Appelle l'API FMI pour récupérer les informations sur l'action
     */
    private Optional<JsonNode> getStockDescription(String ticker) throws IOException, UnauthorizedException {
        String FMI_API_URL = String.format("https://financialmodelingprep.com/api/v3/profile/%s?apikey=%s", ticker, FMI_API_TOKEN);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(FMI_API_URL);

        try {
            logger.debug("Calling FMI API");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                return Optional.of(objectMapper.readTree(responseBody).path(0));

            } else if (response.getStatusLine().getStatusCode() == 404) {
                return Optional.empty();

            } else {
                throw new UnauthorizedException(String.format("Failed to get stock price for ticker '%s'", ticker));
            }

        } catch (IOException e) {
            throw new IOException(String.format("Failed to get stock price for ticker '%s'", ticker));
        }
    }

    /**
     * Appelle l'API ALPHAVANTAGE pour récupérer les actions en tendance.
     */
    public StockTrendListDto getTrends() throws UnauthorizedException, IOException {
        Optional<StockTrendList> optStockTrendList = stockTrendListRepository.findAll().stream().findFirst();
        if (optStockTrendList.isPresent()) {
            StockTrendList trends = optStockTrendList.get();
            LocalDateTime now = LocalDateTime.now();

            if (Duration.between(trends.getLastUpdate(), now).toHours() < TRENDS_FETCH_INTERVAL) {
                return StockTrendListDto.getStockTrendListDto(trends);
            }
        }

        String ALPHAVANTAGE_API_URL = String.format("https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=%s", ALPHAVANTAGE_API_TOKEN);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ALPHAVANTAGE_API_URL);

        try {
            logger.debug("Calling ALPHAVANTAGE API");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                JsonNode gainers = jsonNode.path("top_gainers");
                List<StockTrendDto> gains = new ArrayList<>();
                JsonNode loosers = jsonNode.path("top_losers");
                List<StockTrendDto> pertes = new ArrayList<>();

                extractStockTrendDtoFromReq(gainers, gains);
                extractStockTrendDtoFromReq(loosers, pertes);

                StockTrendList stockTrendList = new StockTrendList(gains, pertes);
                saveStockTrendList(stockTrendList);
                return StockTrendListDto.getStockTrendListDto(stockTrendList);

            } else {
                throw new UnauthorizedException("Failed to fetch stock trends");
            }
        } catch (IOException e) {
            throw new IOException("Failed to fetch stock trends");
        }
    }

    private void extractStockTrendDtoFromReq(JsonNode nodes, List<StockTrendDto> outputList) throws IOException {
        int MAX_TRENDS = 5;
        for (JsonNode node : nodes) {

            if (outputList.size() < MAX_TRENDS) {
                String ticker = node.path("ticker").asText();
                Optional<Stock> optStock = stockRepository.findById(ticker);
                StockDto stock;
                try {
                    if (optStock.isEmpty()) {
                        stock = getStock(ticker, Range.ONE_DAY);
                    } else {
                        stock = StockDto.oneDayDto(optStock.get());
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("0.00%", DecimalFormatSymbols.getInstance(Locale.US));

                    outputList.add(
                            new StockTrendDto(
                                    ticker,
                                    stock.getName(),
                                    node.path("price").asDouble(),
                                    stock.getCurrency(),
                                    node.path("change_amount").asDouble(),
                                    decimalFormat.format(Double.parseDouble(node.path("change_percentage").asText().replace("%", ""))),
                                    node.path("volume").asInt()
                            )
                    );
                } catch (Exception ignored) {
                }
            } else {
                break;
            }
        }
    }

    /**
     * Récupère l'historique de prix d'actions pour un interval donné. Si l'action n'a jamais été récupérée, récupère aussi les informations dessus.
     *
     * @param tickers Liste de tickers des actions à récupérer
     * @param range   1d, 1y
     * @return List<StockListDto>
     */
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

    /**
     * Récupère en base les informations sur une action donnée
     *
     * @param ticker Ticker de l'action à récupérer
     */
    private Optional<Stock> getSavedStock(String ticker) {
        return stockRepository.findById(ticker);
    }

    /**
     * Sauvegarde en base une action
     *
     * @param stock Action à sauvegarder
     */
    @Transactional
    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }

    /**
     * Sauvegarde en base une trend
     * On veut toujours une seule trend, donc on supprime toutes les anciennes quand on en ajoute
     *
     * @param stockTrendList trend
     */
    @Transactional
    public void saveStockTrendList(StockTrendList stockTrendList) {
        stockTrendListRepository.deleteAll();
        stockTrendListRepository.save(stockTrendList);
    }
}

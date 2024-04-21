package projetmicroservices.bourse;

import bourse.dto.StockDto;
import bourse.enums.Range;
import bourse.exceptions.NotFoundException;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Stock;
import bourse.modele.Ticker;
import bourse.repository.StockRepository;
import bourse.service.impl.StockService;
import bourse.service.impl.TickerService;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StockServiceTests {

    @InjectMocks
    private StockService stockService;

    @Mock
    private HttpClient httpClient;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private TickerService tickerService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field stocksFetchInterval = StockService.class.getDeclaredField("STOCKS_FETCH_INTERVAL");
        stocksFetchInterval.setAccessible(true);
        stocksFetchInterval.set(stockService, 1);
        Field yahooApi = StockService.class.getDeclaredField("YAHOO_API_URL");
        yahooApi.setAccessible(true);
        yahooApi.set(stockService, "YAHOO_API_URL");
        Field fmiApi = StockService.class.getDeclaredField("FMI_API_URL");
        fmiApi.setAccessible(true);
        fmiApi.set(stockService, "FMI_API_URL");
    }

    @Test
    void getStock_OK_FIRST_TIME() throws IOException, UnauthorizedException, NotFoundException {
        String ticker = "AAPL";
        Range range = Range.ONE_YEAR;

        String mockJsonResponse = "{" +
                "    \"chart\": {" +
                "        \"result\": [" +
                "            {" +
                "                \"meta\": {" +
                "                    \"currency\": \"USD\"," +
                "                    \"symbol\": \"MSFT\"," +
                "                    \"exchangeName\": \"NMS\"," +
                "                    \"fullExchangeName\": \"NasdaqGS\"," +
                "                    \"instrumentType\": \"EQUITY\"," +
                "                    \"firstTradeDate\": 511108200," +
                "                    \"regularMarketTime\": 1713200811," +
                "                    \"hasPrePostMarketData\": true," +
                "                    \"gmtoffset\": -14400," +
                "                    \"timezone\": \"EDT\"," +
                "                    \"exchangeTimezoneName\": \"America/New_York\"," +
                "                    \"regularMarketPrice\": 420.48," +
                "                    \"fiftyTwoWeekHigh\": 426.82," +
                "                    \"fiftyTwoWeekLow\": 419.365," +
                "                    \"regularMarketDayHigh\": 426.82," +
                "                    \"regularMarketDayLow\": 419.365," +
                "                    \"regularMarketVolume\": 7969786," +
                "                    \"chartPreviousClose\": 286.14," +
                "                    \"priceHint\": 2," +
                "                    \"currentTradingPeriod\": {" +
                "                        \"pre\": {" +
                "                            \"timezone\": \"EDT\"," +
                "                            \"start\": 1713168000," +
                "                            \"end\": 1713187800," +
                "                            \"gmtoffset\": -14400" +
                "                        }," +
                "                        \"regular\": {" +
                "                            \"timezone\": \"EDT\"," +
                "                            \"start\": 1713187800," +
                "                            \"end\": 1713211200," +
                "                            \"gmtoffset\": -14400" +
                "                        }," +
                "                        \"post\": {" +
                "                            \"timezone\": \"EDT\"," +
                "                            \"start\": 1713211200," +
                "                            \"end\": 1713225600," +
                "                            \"gmtoffset\": -14400" +
                "                        }" +
                "                    }," +
                "                    \"dataGranularity\": \"1d\"," +
                "                    \"range\": \"1y\"," +
                "                    \"validRanges\": [" +
                "                        \"1d\"," +
                "                        \"5d\"," +
                "                        \"1mo\"," +
                "                        \"3mo\"," +
                "                        \"6mo\"," +
                "                        \"1y\"," +
                "                        \"2y\"," +
                "                        \"5y\"," +
                "                        \"10y\"," +
                "                        \"ytd\"," +
                "                        \"max\"" +
                "                    ]" +
                "                }," +
                "                \"timestamp\": [" +
                "                    1681738200" +
                "                ]," +
                "                \"indicators\": {" +
                "                    \"quote\": [" +
                "                        {" +
                "                            \"low\": [" +
                "                                286.1600036621094" +
                "                            ]," +
                "                            \"open\": [" +
                "                                289.92999267578125" +
                "                            ]," +
                "                            \"close\": [" +
                "                                288.79998779296875" +
                "                            ]," +
                "                            \"volume\": [" +
                "                                23836200" +
                "                            ]," +
                "                            \"high\": [" +
                "                                291.6000061035156" +
                "                            ]" +
                "                        }" +
                "                    ]," +
                "                    \"adjclose\": [" +
                "                        {" +
                "                            \"adjclose\": [" +
                "                                286.4490051269531" +
                "                            ]" +
                "                        }" +
                "                    ]" +
                "                }" +
                "            }" +
                "        ]," +
                "        \"error\": null" +
                "    }" +
                "}";

        Mockito.when(stockRepository.findById(ticker)).thenReturn(Optional.empty());

        HttpResponse mockResponseYahoo = Mockito.mock(HttpResponse.class);
        StatusLine statusLine = Mockito.mock(StatusLine.class);

        Mockito.when(mockResponseYahoo.getStatusLine()).thenReturn(statusLine);
        Mockito.when(statusLine.getStatusCode()).thenReturn(200);
        Mockito.when(mockResponseYahoo.getEntity()).thenReturn(new StringEntity(mockJsonResponse));

        HttpResponse mockResponseFMI = Mockito.mock(HttpResponse.class);
        StatusLine statusLineFMI = Mockito.mock(StatusLine.class);
        Mockito.when(mockResponseFMI.getStatusLine()).thenReturn(statusLineFMI);
        Mockito.when(statusLineFMI.getStatusCode()).thenReturn(404);

        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenAnswer(invocation -> {
            HttpGet httpGet = invocation.getArgument(0);
            if (httpGet.getURI().toString().startsWith("YAHOO_API_URL")) {
                return mockResponseYahoo;
            } else {
                return mockResponseFMI;
            }
        });

        Mockito.when(tickerService.getTickerOpt(ticker)).thenReturn(
                Optional.ofNullable(Ticker.builder()
                        .ticker(ticker)
                        .Name("Apple Inc.")
                        .Category("Technology")
                        .build()
                )
        );

        StockDto stockDto = stockService.getStock(ticker, range);
        Assertions.assertEquals("AAPL", stockDto.getTicker());
        Assertions.assertEquals("Apple Inc.", stockDto.getName());
        Assertions.assertEquals("Technology", stockDto.getCategory());
        Assertions.assertEquals(420.48, stockDto.getPrice());
    }

    @Test
    void getStock_OK_IN_CACHE() throws IOException, UnauthorizedException, NotFoundException {
        String ticker = "AAPL";
        Range range = Range.ONE_YEAR;

        Stock s = Stock.builder()
                .lastOneYearUpdate(LocalDateTime.now())
                .ticker(ticker)
                .price(420.48)
                .name("Apple Inc.")
                .category("Technology")
                .build();

        Mockito.when(stockRepository.findById(ticker)).thenReturn(Optional.of(s));

        StockDto stockDto = stockService.getStock(ticker, range);
        Assertions.assertEquals("AAPL", stockDto.getTicker());
        Assertions.assertEquals("Apple Inc.", stockDto.getName());
        Assertions.assertEquals("Technology", stockDto.getCategory());
        Assertions.assertEquals(420.48, stockDto.getPrice());
    }
}

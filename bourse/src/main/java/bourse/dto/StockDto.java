package bourse.dto;

import bourse.modele.Stock;
import bourse.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockDto {

    private String ticker;
    private String name;
    private String exchange;
    private String exchangeShortName;
    private String category;
    private String description;
    private Double price;
    private Integer marketCap;
    private String currency;
    private String website;
    private String ceo;
    private String history;

    public static StockDto oneDayDto(Stock stock) {
        return new StockDto(
                stock.getTicker(),
                stock.getName(),
                stock.getExchange(),
                stock.getExchangeShortName(),
                stock.getCategory(),
                stock.getDescription(),
                stock.getPrice(),
                stock.getMarketCap(),
                stock.getCurrency(),
                stock.getWebsite(),
                stock.getCeo(),
                stock.getOneDayHistory()
        );
    }

    public static StockDto oneYearDto(Stock stock) {
        return new StockDto(
                stock.getTicker(),
                stock.getName(),
                stock.getExchange(),
                stock.getExchangeShortName(),
                stock.getCategory(),
                stock.getDescription(),
                stock.getPrice(),
                stock.getMarketCap(),
                stock.getCurrency(),
                stock.getWebsite(),
                stock.getCeo(),
                stock.getOneYearHistory()
        );
    }

    public Object getHistory() {
        return JsonUtils.fromJson(history, Object.class);
    }
}

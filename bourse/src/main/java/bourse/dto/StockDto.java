package bourse.dto;

import bourse.modele.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockDto {

    private String ticker;
    private String name;
    private String exchangeCode;
    private String category;
    private String description;
    private Double price;
    private String currency;
    private String history;

    public static StockDto oneDayDto(Stock stock) {
        return new StockDto(
                stock.getTicker(),
                stock.getName(),
                stock.getExchangeCode(),
                stock.getCategory(),
                stock.getDescription(),
                stock.getPrice(),
                stock.getCurrency(),
                stock.getOneDayHistory()
        );
    }

    public static StockDto oneYearDto(Stock stock) {
        return new StockDto(
                stock.getTicker(),
                stock.getName(),
                stock.getExchangeCode(),
                stock.getCategory(),
                stock.getDescription(),
                stock.getPrice(),
                stock.getCurrency(),
                stock.getOneYearHistory()
        );
    }
}

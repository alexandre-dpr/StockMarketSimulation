package bourse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockTrendDto {

    private String ticker;

    private String name;

    private double price;

    private String currency;

    private double changeAmount;

    private String changePercentage;

    private int volume;
}

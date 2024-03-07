package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockPerformanceDto {

    private String ticker;

    private Double price;

    private Integer quantity;

    private PerformanceDto performance;
}

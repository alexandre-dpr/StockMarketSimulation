package bourse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockListDto {
    private String name;
    private StockDto stock;
}

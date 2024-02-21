package bourse.dto;

import bourse.modele.StockTrendList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StockTrendListDto {

    private LocalDateTime lastUpdate;

    private List<StockTrendDto> gainersJson;

    private List<StockTrendDto> loosersJson;

    public static StockTrendListDto getStockTrendListDto(StockTrendList stockTrendList) {
        return new StockTrendListDto(
                stockTrendList.getLastUpdate(),
                stockTrendList.getGainers(),
                stockTrendList.getLoosers()
        );
    }
}


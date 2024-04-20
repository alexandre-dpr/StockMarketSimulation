package bourse.service;

import bourse.dto.StockDto;
import bourse.dto.StockTrendListDto;
import bourse.enums.Range;
import bourse.exceptions.NotFoundException;
import bourse.exceptions.UnauthorizedException;
import bourse.modele.Stock;
import bourse.modele.StockTrendList;
import jakarta.transaction.Transactional;

import java.io.IOException;

public interface IStockService {
    StockDto getStock(String ticker, Range range) throws IOException, UnauthorizedException, NotFoundException;

    StockTrendListDto getTrends() throws UnauthorizedException, IOException;

    @Transactional
    void saveStock(Stock stock);

    @Transactional
    void saveStockTrendList(StockTrendList stockTrendList);
}

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
    /**
     * Récupère l'historique de prix d'une action pour un interval donné. Si l'action n'a jamais été récupérée, récupère aussi les informations dessus.
     *
     * @param ticker Ticker de l'action à récupérer
     * @param range  1d, 1y
     * @return StockDto
     */
    StockDto getStock(String ticker, Range range) throws IOException, UnauthorizedException, NotFoundException;

    /**
     * Appelle l'API ALPHAVANTAGE pour récupérer les actions en tendance.
     */
    StockTrendListDto getTrends() throws UnauthorizedException, IOException;

    /**
     * Sauvegarde en base une action
     *
     * @param stock Action à sauvegarder
     */
    @Transactional
    void saveStock(Stock stock);

    /**
     * Sauvegarde en base une trend
     * On veut toujours une seule trend, donc on supprime toutes les anciennes quand on en ajoute
     *
     * @param stockTrendList trend
     */
    @Transactional
    void saveStockTrendList(StockTrendList stockTrendList);
}

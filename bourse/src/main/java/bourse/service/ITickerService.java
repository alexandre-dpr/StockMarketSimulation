package bourse.service;

import bourse.modele.Ticker;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ITickerService {
    /**
     * Retourne une liste de Ticker dont le nom est similaire à celui passé
     *
     * @param name nom du ticker
     * @param page numéro de la page
     * @return liste de Ticker
     */
    Page<Ticker> findTickerByName(String name, int page);

    /**
     * Retourne le Ticker correspondant s'il existe
     *
     * @param ticker nom du ticker
     * @return Ticker
     */
    Optional<Ticker> getTickerOpt(String ticker);

    /**
     * Retourne la liste des Ticker
     *
     * @param page numéro de la page
     * @return liste de Ticker
     */
    Page<Ticker> getStocksList(int page);

    /**
     * Sauvegarde un Ticker
     *
     * @param ticker Ticker
     */
    @Transactional
    void saveTicker(Ticker ticker);
}

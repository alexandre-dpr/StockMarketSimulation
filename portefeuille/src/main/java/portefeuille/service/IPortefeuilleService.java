package portefeuille.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.StockPerformanceDto;
import portefeuille.exceptions.*;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;

import java.util.List;

public interface IPortefeuilleService {
    /**
     * Crée un portefeuille pour un utilisateur
     *
     * @param username le nom de l'utilisateur
     * @return le portefeuille créé
     * @throws WalletAlreadyCreatedException si le portefeuille existe déjà
     */
    @Transactional
    PortefeuilleDto creerPortefeuille(String username) throws WalletAlreadyCreatedException;

    /**
     * Retourne les informations calculées du portefeuille pour l'utilisateur donné, s'il n'existe pas, il le crée
     *
     * @param username le nom de l'utilisateur
     * @return le portefeuille de l'utilisateur
     * @throws InterruptedException          si la connexion est perdue avec le service en charge de la récupération du prix
     * @throws WalletAlreadyCreatedException si le portefeuille existe déjà
     */
    PortefeuilleDto getPortefeuilleDto(String username) throws InterruptedException, WalletAlreadyCreatedException;

    /**
     * Retourne les informations calculées du portefeuille pour l'utilisateur donné
     *
     * @param p            le portefeuille
     * @param priceService le service en charge de la récupération du prix
     * @return le portefeuille de l'utilisateur
     * @throws InterruptedException si la connexion est perdue avec le service en charge de la récupération du prix
     */
    PortefeuilleDto getPortefeuilleDto(Portefeuille p, IPriceService priceService) throws InterruptedException;

    /**
     * Calcule la performance d'une action
     *
     * @param action       l'action
     * @param priceService le service en charge de la récupération du prix
     * @return la performance de l'action
     * @throws InterruptedException si la connexion est perdue avec le service en charge de la récupération du prix
     */
    StockPerformanceDto getStockPerformance(Mouvement action, IPriceService priceService) throws InterruptedException;

    /**
     * Calcule la performance d'une action
     *
     * @param ticker   le ticker de l'action
     * @param username le nom de l'utilisateur
     * @return la performance de l'action
     * @throws InterruptedException si la connexion est perdue avec le service en charge de la récupération du prix
     * @throws NotFoundException    si l'action n'existe pas
     */
    StockPerformanceDto getStockPerformance(String ticker, String username) throws InterruptedException, NotFoundException;

    /**
     * Retourne l'historique des actions de l'utilisateur
     *
     * @param username le nom de l'utilisateur
     * @return l'historique des actions de l'utilisateur
     * @throws NotFoundException si l'utilisateur n'existe pas
     */
    HistoryDto getHistorique(String username) throws NotFoundException;

    /**
     * Achète une action pour l'utilisateur
     *
     * @param username le nom de l'utilisateur
     * @param ticker   le ticker de l'action
     * @param quantity la quantité d'actions à acheter
     * @throws NotFoundException          si l'action n'existe pas
     * @throws InsufficientFundsException si l'utilisateur n'a pas assez d'argent pour acheter l'action
     * @throws InterruptedException       si la connexion est perdue avec le service en charge de la récupération du prix
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void acheterAction(String username, String ticker, int quantity) throws NotFoundException, InsufficientFundsException, InterruptedException;

    /**
     * Vend une action pour l'utilisateur
     *
     * @param username le nom de l'utilisateur
     * @param ticker   le ticker de l'action
     * @param quantity la quantité d'actions à vendre
     * @throws NotFoundException        si l'action n'existe pas
     * @throws NotEnoughStocksException si l'utilisateur n'a pas assez d'actions pour vendre
     * @throws InterruptedException     si la connexion est perdue avec le service en charge de la récupération du prix
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void vendreAction(String username, String ticker, int quantity) throws NotFoundException, NotEnoughStocksException, InterruptedException;

    /**
     * Ajoute une action à la liste des favoris de l'utilisateur
     *
     * @param ticker   le ticker de l'action
     * @param username le nom de l'utilisateur
     * @throws TooManyFavorites si l'utilisateur a déjà trop d'actions en favoris
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void ajouterFavori(String ticker, String username) throws TooManyFavorites;

    /**
     * Supprime une action de la liste des favoris de l'utilisateur
     *
     * @param ticker   le ticker de l'action
     * @param username le nom de l'utilisateur
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void supprimerFavori(String ticker, String username);

    /**
     * Retourne la liste des portefeuilles
     *
     * @return la liste des portefeuilles
     */
    List<Portefeuille> getAllPortefeuilles();

    /**
     * Sauvegarde un portefeuille
     *
     * @param p le portefeuille à sauvegarder
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void savePortefeuille(Portefeuille p);
}

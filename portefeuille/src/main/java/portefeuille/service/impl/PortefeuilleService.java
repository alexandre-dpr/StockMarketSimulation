package portefeuille.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.config.Constants;
import portefeuille.dto.*;
import portefeuille.enums.TypeMouvement;
import portefeuille.exceptions.*;
import portefeuille.modele.Mouvement;
import portefeuille.modele.PerformanceHistory;
import portefeuille.modele.Portefeuille;
import portefeuille.repository.MouvementRepository;
import portefeuille.repository.PortefeuilleRepository;
import portefeuille.service.IPerformanceHistoryService;
import portefeuille.service.IPortefeuilleService;
import portefeuille.service.IPriceService;
import portefeuille.service.IRankService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortefeuilleService implements IPortefeuilleService {

    private final PortefeuilleRepository portefeuilleRepository;

    private final MouvementRepository mouvementRepository;

    private final IPerformanceHistoryService performanceHistoryService;

    private final DirectPriceService directPriceService;

    private final IRankService rankService;

    @Autowired
    public PortefeuilleService(PortefeuilleRepository portefeuilleRepository, MouvementRepository mouvementRepository, IPerformanceHistoryService performanceHistoryService, DirectPriceService directPriceService, @Lazy IRankService rankService) {
        this.portefeuilleRepository = portefeuilleRepository;
        this.mouvementRepository = mouvementRepository;
        this.performanceHistoryService = performanceHistoryService;
        this.directPriceService = directPriceService;
        this.rankService = rankService;
    }

    @Override
    @Transactional
    public PortefeuilleDto creerPortefeuille(String username) throws WalletAlreadyCreatedException {
        Optional<Portefeuille> optPortefeuille = portefeuilleRepository.getPortefeuille(username);

        if (optPortefeuille.isEmpty()) {
            Portefeuille p = Portefeuille.builder()
                    .username(username)
                    .solde(Constants.STARTING_BALANCE)
                    .rank(rankService.getDefaultRank())
                    .build();

            portefeuilleRepository.save(p);
            return PortefeuilleDto.createPortefeuilleDto(p.getSolde(), null, null, p.getSolde(), null, p.getRank().getRank(), null);

        } else {
            throw new WalletAlreadyCreatedException("Portefeuille déjà existant");
        }
    }

    /**
     * Retourne les informations calculées du portefeuille pour l'utilisateur donné
     *
     * @param username Username associé au portefeuille
     * @return PortefeuilleDto
     * @throws InterruptedException Si la connexion est perdue avec le service en charge de la récupération du prix
     */
    @Override
    public PortefeuilleDto getPortefeuilleDto(String username) throws InterruptedException, WalletAlreadyCreatedException {
        Optional<Portefeuille> portefeuilleOptional = portefeuilleRepository.getPortefeuille(username);
        if (portefeuilleOptional.isPresent()) {
            return getPortefeuilleDto(portefeuilleOptional.get(), directPriceService);
        } else {
            return creerPortefeuille(username);
        }
    }

    /**
     * Retourne les informations calculées du portefeuille pour l'utilisateur donné
     *
     * @param p            Portefeuille à utiliser pour les calculs
     * @param priceService Service utilisé pour la récupération du prix des actions
     * @return PortefeuilleDto
     * @throws InterruptedException Si la connexion est perdue avec le service en charge de la récupération du prix
     */
    @Override
    public PortefeuilleDto getPortefeuilleDto(Portefeuille p, IPriceService priceService) throws InterruptedException {
        double totalAchats = 0;
        double totalCourant = 0;
        List<StockPerformanceDto> performanceActions = new ArrayList<>();

        for (Mouvement action : p.getActions()) {
            StockPerformanceDto stockPerf = getStockPerformance(action, priceService);
            totalAchats += action.getQuantity() * action.getPrice();
            totalCourant += action.getQuantity() * stockPerf.getPrice();
            performanceActions.add(stockPerf);
        }

        PerformanceDto perf = PerformanceDto.createPerformanceDto(totalAchats, totalCourant);
        performanceHistoryService.savePerformance(perf, p.getUsername());
        List<PerformanceHistory> performanceHistory = performanceHistoryService.getPerformanceHistory(p.getUsername());

        List<FavoriteStockDto> favoriteStockDto = new ArrayList<>();
        for (String ticker : p.getFavoris()) {
            favoriteStockDto.add(
                    new FavoriteStockDto(
                            ticker,
                            priceService.getPrice(ticker)
                    )
            );
        }

        return PortefeuilleDto.createPortefeuilleDto(p.getSolde(), performanceActions, perf, p.getSolde() + totalCourant, performanceHistory, p.getRank().getRank(), favoriteStockDto);
    }

    @Override
    public StockPerformanceDto getStockPerformance(Mouvement action, IPriceService priceService) throws InterruptedException {
        double prixAchat = action.getQuantity() * action.getPrice();
        double currentPrice = priceService.getPrice(action.getTicker());
        double prixActuel = action.getQuantity() * currentPrice;
        PerformanceDto perf = PerformanceDto.createPerformanceDto(prixAchat, prixActuel);
        return new StockPerformanceDto(action.getTicker(), action.getPrice(), currentPrice, action.getQuantity(), perf);
    }

    @Override
    public StockPerformanceDto getStockPerformance(String ticker, String username) throws InterruptedException, NotFoundException {
        Mouvement mouvement = portefeuilleRepository.getStockForUser(ticker, username);
        if (mouvement == null) {
            throw new NotFoundException("L'utilisateur ne possède pas d'action de ce type");
        }
        return getStockPerformance(mouvement, directPriceService);
    }

    @Override
    public HistoryDto getHistorique(String username) throws NotFoundException {
        Optional<Portefeuille> p = portefeuilleRepository.getHistorique(username);
        if (p.isPresent()) {
            return HistoryDto.createHistoryDto(p.get());
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void acheterAction(String username, String ticker, int quantity) throws NotFoundException, InsufficientFundsException, InterruptedException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);
        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();

            double prixAction = directPriceService.getPrice(ticker); // TODO VOIR POUR FRAIS ACHAT

            if (portefeuille.getSolde() >= prixAction * quantity) {

                Mouvement achatHistorique = Mouvement.builder()
                        .time(LocalDateTime.now())
                        .type(TypeMouvement.ACHAT)
                        .ticker(ticker)
                        .price(prixAction)
                        .quantity(quantity)
                        .build();

                portefeuille.getHistorique().add(achatHistorique);

                // Si l'utilisateur a déjà acheté cette action
                Optional<Mouvement> optAction = portefeuilleRepository.getActionPossedee(username, ticker);
                Mouvement achatAction;
                if (optAction.isPresent()) {
                    achatAction = optAction.get().moyennePrixAchat(achatHistorique);
                    mouvementRepository.save(achatAction);
                } else {
                    achatAction = achatHistorique.cloner();
                    portefeuille.getActions().add(achatAction);
                }

                portefeuille.setSolde(portefeuille.getSolde() - prixAction * quantity);
                portefeuilleRepository.save(portefeuille);

            } else {
                throw new InsufficientFundsException("Fonds insuffisants");
            }
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void vendreAction(String username, String ticker, int quantity) throws NotFoundException, NotEnoughStocksException, InterruptedException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);

        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();
            Optional<Mouvement> optMouvement = portefeuilleRepository.getActionPossedee(username, ticker);

            if (optMouvement.isPresent()) {
                Mouvement actionPossedee = optMouvement.get();

                if (actionPossedee.getQuantity() >= quantity) {

                    double prixAction = directPriceService.getPrice(ticker); // TODO VOIR POUR FRAIS VENTE
                    Mouvement mouvementHistorique = Mouvement.builder()
                            .time(LocalDateTime.now())
                            .type(TypeMouvement.VENTE)
                            .ticker(ticker)
                            .price(prixAction)
                            .quantity(quantity)
                            .build();

                    portefeuille.getHistorique().add(mouvementHistorique);

                    if (actionPossedee.getQuantity() > quantity) {
                        actionPossedee.setQuantity(actionPossedee.getQuantity() - quantity);
                    } else {
                        portefeuille.getActions().remove(actionPossedee);
                        mouvementRepository.delete(actionPossedee);
                    }

                    portefeuille.setSolde(portefeuille.getSolde() + quantity * prixAction);
                    portefeuilleRepository.save(portefeuille);

                } else {
                    throw new NotEnoughStocksException("Pas assez d'actions possédées");
                }
            } else {
                throw new NotFoundException("Action non trouvée");
            }
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void ajouterFavori(String ticker, String username) throws TooManyFavorites {
        if (portefeuilleRepository.getNbFavoris(username) < Constants.MAX_FAVORITE_STOCKS) {
            Portefeuille p = portefeuilleRepository.getPortefeuille(username).orElseThrow();
            if (!p.getFavoris().contains(ticker)) {
                p.getFavoris().add(ticker);
                portefeuilleRepository.save(p);
            }
        } else {
            throw new TooManyFavorites("User already has the maximum favorites number");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void supprimerFavori(String ticker, String username) {
        Portefeuille p = portefeuilleRepository.getPortefeuille(username).orElseThrow();
        p.getFavoris().remove(ticker);
        portefeuilleRepository.save(p);
    }

    @Override
    public List<Portefeuille> getAllPortefeuilles() {
        return portefeuilleRepository.findAll();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void savePortefeuille(Portefeuille p) {
        portefeuilleRepository.save(p);
    }
}

package portefeuille.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.config.Constants;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PerformanceDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.StockPerformanceDto;
import portefeuille.dto.rabbitMq.TickerInfoDto;
import portefeuille.enums.TypeMouvement;
import portefeuille.exceptions.*;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.repository.MouvementRepository;
import portefeuille.repository.PortefeuilleRepository;
import portefeuille.repository.TickerInfoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class PortefeuilleService {

    @Autowired
    PortefeuilleRepository portefeuilleRepository;

    @Autowired
    MouvementRepository mouvementRepository;

    @Autowired
    TickerInfoRepository tickerInfoRepository;

    @Autowired
    RabbitMqSender sender;

    @Transactional
    public PortefeuilleDto creerPortefeuille(String username) throws WalletAlreadyCreatedException {
        Optional<Portefeuille> optPortefeuille = portefeuilleRepository.getPortefeuille(username);

        if (optPortefeuille.isEmpty()) {
            Portefeuille p = Portefeuille.builder()
                    .username(username)
                    .solde(Constants.STARTING_BALANCE)
                    .build();

            portefeuilleRepository.save(p);
            return PortefeuilleDto.createPortefeuilleDto(p.getSolde(), null, null, p.getSolde());

        } else {
            throw new WalletAlreadyCreatedException("Portefeuille déjà existant");
        }
    }

    public PortefeuilleDto getPortefeuille(String username) throws NotFoundException, InterruptedException {
        Optional<Portefeuille> portefeuilleOptional = portefeuilleRepository.getPortefeuille(username);
        if (portefeuilleOptional.isPresent()) {
            Portefeuille p = portefeuilleOptional.get();
            double totalAchats = 0;
            double totalCourant = 0;
            List<StockPerformanceDto> performanceActions = new ArrayList<>();

            for (Mouvement action : p.getActions()) {
                double prixAchat = action.getQuantity() * action.getPrice();
                totalAchats += prixAchat;
                double currentPrice = getPrice(action.getTicker());
                double prixActuel = action.getQuantity() * currentPrice;
                totalCourant += prixActuel;
                PerformanceDto perf = PerformanceDto.createPerformanceDto(prixAchat, prixActuel);
                StockPerformanceDto stockPerf = new StockPerformanceDto(action.getTicker(), action.getPrice(), currentPrice, action.getQuantity(), perf);
                performanceActions.add(stockPerf);
            }

            PerformanceDto perf = PerformanceDto.createPerformanceDto(totalAchats, totalCourant);
            return PortefeuilleDto.createPortefeuilleDto(p.getSolde(), performanceActions, perf, p.getSolde() + totalCourant);
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

    public HistoryDto getHistorique(String username) throws NotFoundException {
        Optional<Portefeuille> p = portefeuilleRepository.getHistorique(username);
        if (p.isPresent()) {
            return HistoryDto.createHistoryDto(p.get());
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void acheterAction(String username, String ticker, int quantity) throws NotFoundException, InsufficientFundsException, InterruptedException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);
        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();

            double prixAction = getPrice(ticker); // TODO VOIR POUR FRAIS ACHAT

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void vendreAction(String username, String ticker, int quantity) throws NotFoundException, NotEnoughStocksException, InterruptedException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);

        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();
            Optional<Mouvement> optMouvement = portefeuilleRepository.getActionPossedee(username, ticker);

            if (optMouvement.isPresent()) {
                Mouvement actionPossedee = optMouvement.get();

                if (actionPossedee.getQuantity() >= quantity) {

                    double prixAction = getPrice(ticker); // TODO VOIR POUR FRAIS VENTE
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public double getPrice(String ticker) throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        sender.send(new TickerInfoDto(uuid, ticker, null));
        while (tickerInfoRepository.findById(uuid).isEmpty()) {
            sleep(2000);
        }
        double price = tickerInfoRepository.findById(uuid).get().getPrice();
        tickerInfoRepository.deleteById(uuid);
        return price;
    }

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

    public void supprimerFavori(String ticker, String username) {
        Portefeuille p = portefeuilleRepository.getPortefeuille(username).orElseThrow();
        p.getFavoris().remove(ticker);
        portefeuilleRepository.save(p);
    }

    public List<String> getFavoris(String username) {
        return portefeuilleRepository.getFavoris(username);
    }
}

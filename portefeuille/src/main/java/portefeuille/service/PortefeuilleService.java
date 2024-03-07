package portefeuille.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.rabbitMq.TickerInfoDto;
import portefeuille.enums.TypeMouvement;
import portefeuille.exceptions.InsufficientFundsException;
import portefeuille.exceptions.NotEnoughStocksException;
import portefeuille.exceptions.NotFoundException;
import portefeuille.exceptions.WalletAlreadyCreatedException;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.repository.MouvementRepository;
import portefeuille.repository.PortefeuilleRepository;
import portefeuille.repository.TickerInfoRepository;

import java.time.LocalDateTime;
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
    CacheService cacheService;
    @Autowired
    RabbitMqSender sender;

    @Autowired
    TickerInfoRepository tickerInfoRepository;

    @Transactional
    public PortefeuilleDto creerPortefeuille(String username) throws WalletAlreadyCreatedException {
        Optional<Portefeuille> optPortefeuille = portefeuilleRepository.getPortefeuille(username);

        if (optPortefeuille.isEmpty()) {
            Portefeuille p = Portefeuille.builder()
                    .username(username)
                    .solde(1000.0)
                    .build();

            portefeuilleRepository.save(p);
            return PortefeuilleDto.createPortefeuilleDto(p);

        } else {
            throw new WalletAlreadyCreatedException("Portefeuille déjà existant");
        }
    }

    // TODO Récupérer les prix actuels pour donner le bénéfice
    public PortefeuilleDto getPortefeuille(String username) throws NotFoundException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);
        if (p.isPresent()) {
            return PortefeuilleDto.createPortefeuilleDto(p.get());
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

            double prixAction = getPrice(ticker); // TODO APPELER SERVICE BOURSE POUR RECUP PRIX, VOIR AUSSI POUR FRAIS ACHAT
            System.out.println("qizn");
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

    @Transactional
    public void vendreAction(String username, String ticker, int quantity) throws NotFoundException, NotEnoughStocksException, InterruptedException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);

        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();
            Optional<Mouvement> optMouvement = portefeuilleRepository.getActionPossedee(username, ticker);

            if (optMouvement.isPresent()) {
                Mouvement actionPossedee = optMouvement.get();

                if (actionPossedee.getQuantity() >= quantity) {

                    double prixAction = getPrice(ticker); // TODO APPELER SERVICE BOURSE POUR RECUP PRIX, VOIR AUSSI POUR FRAIS VENTE
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
    double getPrice(String ticker) throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        sender.send(new TickerInfoDto(uuid,ticker,-10.0));
      //  while (!cacheService.isPresentTickerInfo(uuid)){
     //       sleep(2000);
     //   }
     //   double price = cacheService.getTickerInfo(uuid).price();
     //   cacheService.removeTickerInfo(uuid);

        while (tickerInfoRepository.findById(uuid).isEmpty()){
            sleep(2000);
        }
        double price = tickerInfoRepository.findById(uuid).get().getPrice();
        tickerInfoRepository.deleteById(uuid);
        return price;
    }

}

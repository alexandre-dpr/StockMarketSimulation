package portefeuille.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.enums.TypeMouvement;
import portefeuille.exceptions.InsufficientFundsException;
import portefeuille.exceptions.NotFoundException;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;
import portefeuille.repository.MouvementRepository;
import portefeuille.repository.PortefeuilleRepository;

import java.util.Optional;

@Service
public class PortefeuilleService {

    @Autowired
    PortefeuilleRepository portefeuilleRepository;

    @Autowired
    MouvementRepository mouvementRepository;

    public PortefeuilleDto creerPortefeuille(String username) {
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(1000.0)
                .build();

        portefeuilleRepository.save(p);
        return PortefeuilleDto.createPortefeuilleDto(p);
    }

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

    @Transactional
    public void acheterAction(String username, String ticker, int quantity) throws NotFoundException, InsufficientFundsException {
        Optional<Portefeuille> p = portefeuilleRepository.getPortefeuille(username);
        if (p.isPresent()) {
            Portefeuille portefeuille = p.get();
            Double prixAction = 110.0; // TODO APPELER SERVICE BOURSE POUR RECUP PRIX

            if (portefeuille.getSolde() >= prixAction) {

                Mouvement achatHistorique = Mouvement.builder()
                        .type(TypeMouvement.ACHAT)
                        .ticker(ticker)
                        .buyPrice(prixAction)
                        .quantity(quantity)
                        .build();

                mouvementRepository.save(achatHistorique);
                portefeuille.getHistorique().add(achatHistorique);

                // Si l'utilisateur a déjà acheté cette action
                Optional<Mouvement> optAction = portefeuilleRepository.getActionPossedee(username, ticker);
                Mouvement achatAction;
                if (optAction.isPresent()) {
                    achatAction = optAction.get().moyennePrixAchat(achatHistorique);
                } else {
                    achatAction = achatHistorique.cloner();
                }
                mouvementRepository.save(achatAction);
                portefeuille.getActions().add(achatAction);

                portefeuille.setSolde(portefeuille.getSolde() - prixAction);
                portefeuilleRepository.save(portefeuille);

            } else {
                throw new InsufficientFundsException("Fonds insuffisants");
            }
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

}

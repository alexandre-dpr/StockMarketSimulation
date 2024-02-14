package portefeuille.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portefeuille.repository.PortefeuilleRepository;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.exceptions.NotFoundException;
import portefeuille.modele.Portefeuille;

import java.util.Optional;

@Service
public class PortefeuilleService {

    @Autowired
    PortefeuilleRepository portefeuilleRepository;

    public PortefeuilleDto creerPortefeuille(String username) {
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(1000.0)
                .build();

        portefeuilleRepository.save(p);
        return PortefeuilleDto.createPortefeuilleDto(p);
    }

    public PortefeuilleDto getPortefeuille(String username) throws NotFoundException {
        Optional<Portefeuille> p = portefeuilleRepository.findById(username);
        if (p.isPresent()) {
            return PortefeuilleDto.createPortefeuilleDto(p.get());
        } else {
            throw new NotFoundException("Personne non trouvée");
        }
    }

}

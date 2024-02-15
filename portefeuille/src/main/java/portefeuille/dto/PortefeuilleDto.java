package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PortefeuilleDto {
    private String username;
    private Double solde;
    private List<Mouvement> mouvements;

    public static PortefeuilleDto createPortefeuilleDto(Portefeuille p) {
        return new PortefeuilleDto(
                p.getUsername(),
                p.getSolde(),
                p.getActions() == null ? new ArrayList<>() : p.getActions()
        );
    }
}

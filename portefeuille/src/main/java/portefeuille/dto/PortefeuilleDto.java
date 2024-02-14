package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import portefeuille.modele.Portefeuille;
import portefeuille.modele.Mouvement;

import java.util.List;

@Getter
@AllArgsConstructor
public class PortefeuilleDto {
    private String username;
    private Double solde;
    private List<Mouvement> mouvement;

    public static PortefeuilleDto createPortefeuilleDto(Portefeuille p) {
        return new PortefeuilleDto(
                p.getUsername(),
                p.getSolde(),
                p.getActions()
        );
    }
}

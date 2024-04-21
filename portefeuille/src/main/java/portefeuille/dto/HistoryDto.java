package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;

import java.util.List;

@Getter
@AllArgsConstructor
public class HistoryDto {
    private String username;
    private List<Mouvement> mouvements;

    public static HistoryDto createHistoryDto(Portefeuille p) {
        return new HistoryDto(
                p.getUsername(),
                p.getHistorique()
        );
    }
}

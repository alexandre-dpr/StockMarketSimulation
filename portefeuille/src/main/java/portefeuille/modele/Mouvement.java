package portefeuille.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import portefeuille.enums.TypeMouvement;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mouvement {

    @Id
    @GeneratedValue
    private Integer mouvementId;

    private TypeMouvement type;

    private String ticker;

    private Double buyPrice;

    private Integer quantity;

    @JsonIgnore
    @ManyToOne
    private Portefeuille portefeuille;

    /**
     * Retourne un nouvel objet dont le prix est la moyenne des deux mouvements, et la quantité la somme des deux
     *
     * @param m deuxième mouvement pour faire la moyenne
     * @return nouvel objet mouvement
     */
    public Mouvement moyennePrixAchat(Mouvement m) {
        int quantite = this.quantity + m.quantity;
        Double buyPrice = ((this.buyPrice * this.quantity) + (m.buyPrice * m.quantity)) / quantite;
        return new Mouvement(
                this.mouvementId,
                this.type,
                this.ticker,
                buyPrice,
                quantite,
                portefeuille
        );
    }

    /**
     * Retourne une copie de l'objet, sauf son mouvementId
     *
     * @return Nouvelle instance de Mouvement
     */
    public Mouvement cloner() {
        return Mouvement.builder()
                .type(type)
                .ticker(ticker)
                .buyPrice(buyPrice)
                .quantity(quantity)
                .portefeuille(portefeuille)
                .build();
    }
}

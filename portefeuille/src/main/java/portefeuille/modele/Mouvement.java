package portefeuille.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import portefeuille.enums.TypeMouvement;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mouvementId;

    private LocalDateTime time;

    private TypeMouvement type;

    private String ticker;

    private Double price;

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
        Double buyPrice = ((this.price * this.quantity) + (m.price * m.quantity)) / quantite;
        return new Mouvement(
                this.mouvementId,
                LocalDateTime.now(),
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
                .time(time)
                .type(type)
                .ticker(ticker)
                .price(price)
                .quantity(quantity)
                .portefeuille(portefeuille)
                .build();
    }
}

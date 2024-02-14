package portefeuille.modele;

import portefeuille.enums.TypeMouvement;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Mouvement {

    @Id
    private String username;

    private TypeMouvement type;

    private String ticker;

    private Double prixAchat;

    private Integer quantite;
}

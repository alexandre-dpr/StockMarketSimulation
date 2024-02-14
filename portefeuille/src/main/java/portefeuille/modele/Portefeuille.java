package portefeuille.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Portefeuille {

    @Id
    private String username;

    private Double solde;

    @OneToMany
    List<Mouvement> actions;

    @OneToMany
    List<Mouvement> historique;
}

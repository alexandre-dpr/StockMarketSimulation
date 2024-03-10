package portefeuille.modele;

import jakarta.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL)
    List<Mouvement> actions;

    @OneToMany(cascade = CascadeType.ALL)
    List<Mouvement> historique;

    @ElementCollection
    @CollectionTable(name = "favoris", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "favori")
    List<String> favoris;
}

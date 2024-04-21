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
    private List<Mouvement> actions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Mouvement> historique;

    @ElementCollection
    @CollectionTable(name = "favoris", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "favori")
    private List<String> favoris;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Rank rank;
}

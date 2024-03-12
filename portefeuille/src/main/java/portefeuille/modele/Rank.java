package portefeuille.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "portefeuille_rank")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "portefeuille_rank_gen")
    @TableGenerator(name = "portefeuille_rank_gen", table = "portefeuille_rank_seq", allocationSize = 1)
    private Long id;

    @Column(name = "rank_number")
    private Long rank;

    private double walletValue;

    private String percentage;

    @OneToOne(mappedBy = "rank")
    private Portefeuille portefeuille;
}

package portefeuille.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class TickerInfo {
    @Id
    String uuid;

    String ticker;

    Double price;
}

package bourse.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Ticker {

    @Id
    private String ticker;

    private String Name;

    private String Exchange;

    private String Category;

    private String Country;
}

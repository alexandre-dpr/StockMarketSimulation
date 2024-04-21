package bourse.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Stock {

    @Id
    private String ticker;

    private String name;

    private String exchange;

    private String exchangeShortName;

    private String category;

    @Column(length = 5000)
    private String description;

    private Double price;

    private Long marketCap;

    private String currency;

    private String website;

    private String ceo;

    @Column(length = 100000)
    private String oneDayHistory;

    private LocalDateTime lastOneDayUpdate;

    @Column(length = 100000)
    private String oneYearHistory;

    private LocalDateTime lastOneYearUpdate;

    @Override
    public String toString() {
        String res = ticker + " - " + name + "\n";
        res += "Exchange code: " + exchange + "\n";
        res += "Price: " + price + " " + currency;
        return res;
    }
}

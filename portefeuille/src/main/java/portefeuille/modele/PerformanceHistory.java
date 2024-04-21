package portefeuille.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PerformanceHistory {

    public PerformanceHistory(String percentage, double value, String username) {
        this.percentage = percentage;
        this.value = value;
        this.username = username;
        this.date = LocalDateTime.now();
    }

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    private String percentage;

    private double value;

    private LocalDateTime date;

    @JsonIgnore
    private String username;
}

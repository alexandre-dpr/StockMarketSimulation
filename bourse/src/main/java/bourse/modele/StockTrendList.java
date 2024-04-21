package bourse.modele;

import bourse.dto.StockTrendDto;
import bourse.util.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class StockTrendList {

    public StockTrendList(List<StockTrendDto> gainers, List<StockTrendDto> loosers) {
        this.lastUpdate = LocalDateTime.now();
        setGainers(gainers);
        setLoosers(loosers);
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime lastUpdate;

    @Column(columnDefinition = "TEXT")
    private String gainersJson;

    @Column(columnDefinition = "TEXT")
    private String loosersJson;

    public List<StockTrendDto> getGainers() {
        return JsonUtils.fromJsonList(gainersJson, StockTrendDto.class);
    }

    public void setGainers(List<StockTrendDto> gainers) {
        this.gainersJson = JsonUtils.toJson(gainers);
    }

    public List<StockTrendDto> getLoosers() {
        return JsonUtils.fromJsonList(loosersJson, StockTrendDto.class);
    }

    public void setLoosers(List<StockTrendDto> loosers) {
        this.loosersJson = JsonUtils.toJson(loosers);
    }

}

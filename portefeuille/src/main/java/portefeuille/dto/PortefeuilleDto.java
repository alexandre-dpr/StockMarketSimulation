package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import portefeuille.modele.PerformanceHistory;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PortefeuilleDto {
    private Double solde;
    private List<StockPerformanceDto> actions;
    private PerformanceDto performance;
    private Double totalValue;
    List<PerformanceHistory> performanceHistory;
    Long rank;
    List<FavoriteStockDto> favoris;

    public static PortefeuilleDto createPortefeuilleDto(double solde, List<StockPerformanceDto> actions, PerformanceDto performance, Double totalValue, List<PerformanceHistory> performanceHistory, Long rank, List<FavoriteStockDto> favoris) {
        return new PortefeuilleDto(
                solde,
                actions == null ? new ArrayList<>() : actions,
                performance == null ? PerformanceDto.createPerformanceDto(0, 0) : performance,
                totalValue,
                performanceHistory == null ? new ArrayList<>() : performanceHistory,
                rank,
                favoris == null ? new ArrayList<>() : favoris
        );
    }
}

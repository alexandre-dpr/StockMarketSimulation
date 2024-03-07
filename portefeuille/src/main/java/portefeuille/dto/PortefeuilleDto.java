package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PortefeuilleDto {
    private Double solde;
    private List<StockPerformanceDto> actions;
    private PerformanceDto performance;

    public static PortefeuilleDto createPortefeuilleDto(double solde, List<StockPerformanceDto> actions, PerformanceDto performance) {
        return new PortefeuilleDto(
                solde,
                actions == null ? new ArrayList<>() : actions,
                performance == null ? PerformanceDto.createPerformanceDto(0, 0) : performance
        );
    }
}

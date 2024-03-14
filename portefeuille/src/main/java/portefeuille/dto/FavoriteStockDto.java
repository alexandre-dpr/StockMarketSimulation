package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteStockDto {

    private String ticker;

    private double price;
}

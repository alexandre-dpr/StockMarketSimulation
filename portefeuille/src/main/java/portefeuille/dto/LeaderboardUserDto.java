package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardUserDto {

    private Long rank;

    private String username;

    private String percentage;

    private double totalValue;
}

package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LeaderboardDto {

    private List<LeaderboardUserDto> leaderboard;

    private LeaderboardUserDto user;
}

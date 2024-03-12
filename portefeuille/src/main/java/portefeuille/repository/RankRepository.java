package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portefeuille.config.Constants;
import portefeuille.dto.LeaderboardUserDto;
import portefeuille.modele.Rank;

import java.util.List;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

    /**
     * Retourne (rank + 1) du dernier qui a un profit > 0
     * S'il n'existe pas, retourne 1
     */
    @Query(value = "SELECT COALESCE((SELECT CASE " +
            "           WHEN r.wallet_value = " + Constants.STARTING_BALANCE + " THEN r.rank_number " +
            "           ELSE r.rank_number + 1 " +
            "           END " +
            "FROM portefeuille_rank r " +
            "WHERE r.wallet_value >= " + Constants.STARTING_BALANCE +
            " ORDER BY r.wallet_value" +
            " LIMIT 1), 1) AS rank_number", nativeQuery = true)
    Long getDefaultRank();

    /**
     * Retourne les 15 premiers du leaderboard
     */
    @Query("SELECT NEW portefeuille.dto.LeaderboardUserDto(r.rank, r.portefeuille.username, r.percentage, r.walletValue) FROM Rank r WHERE r.rank <= 15")
    List<LeaderboardUserDto> getLeaderboard();

    @Query("SELECT NEW portefeuille.dto.LeaderboardUserDto(r.rank, r.portefeuille.username, r.percentage, r.walletValue) FROM Rank r WHERE r.portefeuille.username=:username")
    LeaderboardUserDto getLeaderboardPosition(@Param("username") String username);
}

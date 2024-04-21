package portefeuille.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.dto.LeaderboardDto;
import portefeuille.modele.Rank;

public interface IRankService {
    /**
     * Calcule les rangs des utilisateurs
     *
     * @throws InterruptedException si la récupération des prix est interrompue
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void calculateRanks() throws InterruptedException;

    /**
     * Récupère le rang par défaut
     *
     * @return le rang par défaut
     */
    Rank getDefaultRank();

    /**
     * Retourne les 15 premiers du leaderboard et l'utilisateur (au cas où il est hors des 15 premiers)
     *
     * @param username le nom d'utilisateur
     * @return le leaderboard
     */
    LeaderboardDto getLeaderboard(String username);
}

package portefeuille.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.config.Constants;
import portefeuille.dto.LeaderboardDto;
import portefeuille.dto.LeaderboardUserDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.modele.Portefeuille;
import portefeuille.modele.Rank;
import portefeuille.repository.RankRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankService {

    @Autowired
    private RankRepository rankRepository;

    @Autowired
    private PortefeuilleService portefeuilleService;

    @Autowired
    private CachedPriceService cachedPriceService;

    public record Pair(Portefeuille pf, PortefeuilleDto dto) {
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void calculateRanks() throws InterruptedException {
        List<Portefeuille> pfList = portefeuilleService.getAllPortefeuilles();
        List<Pair> pairList = new ArrayList<>();
        for (Portefeuille p : pfList) {
            pairList.add(new Pair(p, portefeuilleService.getPortefeuilleDto(p, cachedPriceService)));
        }
        pairList.sort((p1, p2) -> p2.dto.getTotalValue().compareTo(p1.dto.getTotalValue()));

        long currRank = 0L;
        Double currValue = null;
        for (Pair pair : pairList) {
            if (!pair.dto.getTotalValue().equals(currValue)) {
                currValue = pair.dto.getTotalValue();
                currRank++;
            }
            pair.pf.setRank(
                    Rank.builder()
                            .rank(currRank)
                            .walletValue(currValue)
                            .percentage(pair.dto.getPerformance().getPercentage())
                            .build()
            );
            portefeuilleService.savePortefeuille(pair.pf);
        }
        cachedPriceService.clearCache();
    }

    public Rank getDefaultRank() {
        return Rank.builder()
                .rank(rankRepository.getDefaultRank())
                .walletValue(Constants.STARTING_BALANCE)
                .percentage("0.00%")
                .build();
    }

    /**
     * Retourne les 15 premiers du leaderboard et l'utilisateur (au cas où il est hors des 15 premiers)
     */
    public LeaderboardDto getLeaderboard(String username) {
        List<LeaderboardUserDto> leaderboard = rankRepository.getLeaderboard().stream()
                .limit(15)
                .sorted((u1, u2) -> Double.compare(u2.getTotalValue(), u1.getTotalValue()))
                .toList();
        LeaderboardUserDto user = rankRepository.getLeaderboardPosition(username);
        return new LeaderboardDto(leaderboard, user);
    }

}

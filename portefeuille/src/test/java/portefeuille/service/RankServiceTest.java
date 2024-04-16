package portefeuille.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import portefeuille.config.Constants;
import portefeuille.dto.LeaderboardDto;
import portefeuille.dto.LeaderboardUserDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.modele.Portefeuille;
import portefeuille.modele.Rank;
import portefeuille.repository.RankRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class RankServiceTest {

    @Mock
    private RankRepository rankRepository;

    @Mock
    private PortefeuilleService portefeuilleService;

    @Mock
    private CachedPriceService cachedPriceService;

    @InjectMocks
    private RankService service;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void calculateRanks_OK() throws InterruptedException {
        Portefeuille portefeuille = Portefeuille.builder()
                .build();
        List<Portefeuille> listPortefeuille = new ArrayList<>();
        listPortefeuille.add(portefeuille);
        when(portefeuilleService.getAllPortefeuilles()).thenReturn(listPortefeuille);
        when(portefeuilleService.getPortefeuilleDto(portefeuille,cachedPriceService)).thenReturn(PortefeuilleDto.createPortefeuilleDto(10000.,null,null,10000.,null,1L,null));
        service.calculateRanks();
        verify(portefeuilleService, times(1)).savePortefeuille(any());
        verify(cachedPriceService, times(1)).clearCache();
    }

    @Test
    public void getDefaultRank_OK(){
        Rank rank = Rank.builder()
                .rank(3L)
                .walletValue(Constants.STARTING_BALANCE)
                .percentage("0.00%")
                .build();
        when(rankRepository.getDefaultRank()).thenReturn(3L);
        Rank result = service.getDefaultRank();
        assertEquals(rank,result);
    }

    @Test
    public void getLeaderboard_OK(){
        List<LeaderboardUserDto> listLeaderboardUserDtos = new ArrayList<>();
        LeaderboardUserDto leaderboardUserDto = new LeaderboardUserDto(1L,"username","30%",13000.);
        listLeaderboardUserDtos.add(leaderboardUserDto);
        LeaderboardDto leaderboardDto = new LeaderboardDto(listLeaderboardUserDtos,leaderboardUserDto);
        when(rankRepository.getLeaderboard()).thenReturn(listLeaderboardUserDtos);
        when(rankRepository.getLeaderboardPosition("username")).thenReturn(leaderboardUserDto);
        LeaderboardDto result = service.getLeaderboard("username");
        assertEquals(leaderboardDto.getLeaderboard(),result.getLeaderboard());
        assertEquals(leaderboardDto.getUser(),result.getUser());
    }

}

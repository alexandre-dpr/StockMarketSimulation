package portefeuille.service;


import org.mockito.MockitoAnnotations;
import portefeuille.config.Constants;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.StockPerformanceDto;
import portefeuille.enums.TypeMouvement;
import portefeuille.exceptions.*;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;
import portefeuille.modele.Rank;
import portefeuille.repository.MouvementRepository;
import portefeuille.repository.PortefeuilleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PortefeuilleServiceTest {

    @Mock
    private  PortefeuilleRepository portefeuilleRepository;

    @Mock
    private  MouvementRepository mouvementRepository;

    @Mock
    private  PerformanceHistoryService performanceHistoryService;

    @Mock
    private  DirectPriceService directPriceService;

    @Mock
    private RankService rankService;

    @InjectMocks
    private PortefeuilleService facade;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void creerPortefeuille_OK() throws WalletAlreadyCreatedException {
        String username = "user";
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.empty());
        when(rankService.getDefaultRank()).thenReturn(new Rank());
        PortefeuilleDto portefeuilleDto = facade.creerPortefeuille(username);
        assertEquals(Constants.STARTING_BALANCE, portefeuilleDto.getSolde().doubleValue(),0);

    }

    @Test(expected = WalletAlreadyCreatedException.class)
    public void creerPortefeuille_KO_WalletAlreadyCreatedException() throws WalletAlreadyCreatedException {
        String username = "user";
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        facade.creerPortefeuille(username);
    }

    @Test
    public void getPortefeuilleDto_create_OK() throws WalletAlreadyCreatedException, InterruptedException {
        String username="user";
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.empty());
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.empty());
        when(rankService.getDefaultRank()).thenReturn(new Rank());
        PortefeuilleDto portefeuilleDto = facade.getPortefeuilleDto(username);
        assertEquals(Constants.STARTING_BALANCE, portefeuilleDto.getSolde().doubleValue(),0);
    }
    @Test
    public void getPortefeuilleDto_get_OK() throws WalletAlreadyCreatedException, InterruptedException {
        String username="user";
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));

        PortefeuilleDto portefeuilleDto = facade.getPortefeuilleDto(username);

        assertNotNull(portefeuilleDto);
        assertEquals(p.getSolde(),portefeuilleDto.getSolde());
    }

    @Test
    public void getStockPerformance_OK() throws InterruptedException {
        Mouvement m = Mouvement.builder()
                .ticker("Ticker")
                .price(10.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        StockPerformanceDto stockPerformanceDto =facade.getStockPerformance(m,directPriceService);
        assertEquals(m.getQuantity(),stockPerformanceDto.getQuantity());
    }

    @Test
    public void getStockPerformance2_OK() throws NotFoundException, InterruptedException {
        Mouvement m = Mouvement.builder()
                .ticker("Ticker")
                .price(10.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        when(portefeuilleRepository.getStockForUser("ticker","user")).thenReturn(m);
        StockPerformanceDto stockPerformanceDto = facade.getStockPerformance("ticker","user");
        assertEquals(m.getQuantity(),stockPerformanceDto.getQuantity());

    }

    @Test(expected = NotFoundException.class)
    public void getStockPerformance2_KO_NotFoundException() throws NotFoundException, InterruptedException {
        when(portefeuilleRepository.getStockForUser("ticker","user")).thenReturn(null);
        facade.getStockPerformance("ticker","user");
    }

    @Test
    public void getHistorique_OK() throws NotFoundException {
        String username = "user";
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .build();
        when(portefeuilleRepository.getHistorique(username)).thenReturn(Optional.of(p));
        HistoryDto historyDto = facade.getHistorique(username);
        assertEquals(p.getUsername(),historyDto.getUsername());
        assertEquals(p.getHistorique(),historyDto.getMouvements());
    }

    @Test(expected = NotFoundException.class)
    public void getHistorique_KO_NotFoundException() throws NotFoundException {
        String username = "user";
        when(portefeuilleRepository.getHistorique(username)).thenReturn(Optional.empty());
        facade.getHistorique(username);
    }

    @Test
    public void acheterAction_OK_AlreadyGetAction() throws InterruptedException, InsufficientFundsException, NotFoundException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 2;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        Mouvement m = Mouvement.builder()
                .ticker(ticker)
                .price(30.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(directPriceService.getPrice(ticker)).thenReturn(30.);
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.of(m));
        facade.acheterAction(username,ticker,quantity);
        verify(portefeuilleRepository).save(p);
        assertEquals(9940.0, p.getSolde(),0.1);
    }

    @Test
    public void acheterAction_OK_NotGetAction() throws InterruptedException, InsufficientFundsException, NotFoundException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 2;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(directPriceService.getPrice(ticker)).thenReturn(30.);
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.empty());
        facade.acheterAction(username,ticker,quantity);
        verify(portefeuilleRepository).save(p);
        assertEquals(9940.0, p.getSolde(),0.1);
    }

    @Test(expected = InsufficientFundsException.class)
    public void acheterAction_KO_InsufficientFundsException() throws InterruptedException, InsufficientFundsException, NotFoundException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 2;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(directPriceService.getPrice(ticker)).thenReturn(7000.);
        facade.acheterAction(username,ticker,quantity);
    }

    @Test(expected = NotFoundException.class)
    public void acheterAction_KO_NotFoundException() throws InterruptedException, InsufficientFundsException, NotFoundException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 2;
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.empty());
        facade.acheterAction(username,ticker,quantity);
    }

    @Test
    public void vendreAction_OK_All() throws NotEnoughStocksException, NotFoundException, InterruptedException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 2;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        Mouvement m = Mouvement.builder()
                .ticker(ticker)
                .price(30.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.of(m));
        facade.vendreAction(username,ticker,quantity);
        verify(mouvementRepository).delete(m);
        verify(portefeuilleRepository).save(p);
    }

    @Test
    public void vendreAction_OK_NotAll() throws NotEnoughStocksException, NotFoundException, InterruptedException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 1;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        Mouvement m = Mouvement.builder()
                .ticker(ticker)
                .price(30.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.of(m));
        facade.vendreAction(username,ticker,quantity);
        verify(portefeuilleRepository).save(p);
    }

    @Test(expected = NotEnoughStocksException.class)
    public void vendreAction_KO_NotEnoughStocksException() throws NotEnoughStocksException, NotFoundException, InterruptedException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 4;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        Mouvement m = Mouvement.builder()
                .ticker(ticker)
                .price(30.)
                .quantity(2)
                .type(TypeMouvement.ACHAT)
                .time(LocalDateTime.now())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.of(m));
        facade.vendreAction(username,ticker,quantity);
    }

    @Test(expected = NotFoundException.class)
    public void vendreAction_KO_NotFoundException_Action() throws NotEnoughStocksException, NotFoundException, InterruptedException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 4;
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        when(portefeuilleRepository.getActionPossedee(username,ticker)).thenReturn(Optional.empty());
        facade.vendreAction(username,ticker,quantity);
    }

    @Test(expected = NotFoundException.class)
    public void vendreAction_KO_NotFoundException_User() throws NotEnoughStocksException, NotFoundException, InterruptedException {
        String username = "user";
        String ticker = "ticker";
        int quantity = 4;
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.empty());
        facade.vendreAction(username,ticker,quantity);
    }

    @Test
    public void ajouterFavori_OK() throws TooManyFavorites {
        String username = "username";
        String ticker = "ticker";
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        when(portefeuilleRepository.getNbFavoris(username)).thenReturn(0);
        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        facade.ajouterFavori(ticker,username);
        verify(portefeuilleRepository).save(p);

    }

    @Test(expected = TooManyFavorites.class)
    public void ajouterFavori_KO_TooManyFavorites() throws TooManyFavorites {
        String username = "username";
        String ticker = "ticker";
        when(portefeuilleRepository.getNbFavoris(username)).thenReturn(Constants.MAX_FAVORITE_STOCKS+1);
        facade.ajouterFavori(ticker,username);
    }


    @Test
    public void supprimerFavori_OK(){
        String username = "username";
        String ticker = "ticker";
        Portefeuille p = Portefeuille.builder()
                .username(username)
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>(Arrays.asList(ticker)))
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();

        when(portefeuilleRepository.getPortefeuille(username)).thenReturn(Optional.of(p));
        facade.supprimerFavori(ticker,username);
        verify(portefeuilleRepository).save(p);

    }

    @Test
    public void getAllPortefeuille_OK(){
        Portefeuille p1 = Portefeuille.builder()
                .username("user1")
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        Portefeuille p2 = Portefeuille.builder()
                .username("user2")
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        List<Portefeuille> allPortefeuille = new ArrayList<>(Arrays.asList(p1,p2));
        when(portefeuilleRepository.findAll()).thenReturn(allPortefeuille);
        facade.getAllPortefeuilles();
    }

    @Test
    public void savePortefeuille_OK(){
        Portefeuille p = Portefeuille.builder()
                .username("user")
                .solde(Constants.STARTING_BALANCE)
                .rank(rankService.getDefaultRank())
                .favoris(new ArrayList<>())
                .actions(new ArrayList<>())
                .rank(new Rank())
                .historique(new ArrayList<>())
                .build();
        facade.savePortefeuille(p);
        verify(portefeuilleRepository).save(p);
    }

}
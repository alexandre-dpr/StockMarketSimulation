package portefeuille.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.LeaderboardDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.StockPerformanceDto;
import portefeuille.dto.request.TickerReqDto;
import portefeuille.dto.request.TransactionActionReqDto;
import portefeuille.exceptions.*;
import portefeuille.service.PortefeuilleService;
import portefeuille.service.RankService;

@RestController
@RequestMapping("/portefeuille")
public class PortefeuilleController {

    @Autowired
    private PortefeuilleService portefeuilleService;

    @Autowired
    private RankService rankService;

    @GetMapping
    public ResponseEntity<PortefeuilleDto> getPortefeuille(Authentication authentication) throws InterruptedException, WalletAlreadyCreatedException {
        return ResponseEntity.ok().body(portefeuilleService.getPortefeuilleDto(authentication.getName()));
    }

    @GetMapping("/historique")
    public ResponseEntity<HistoryDto> getHistorique(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok().body(portefeuilleService.getHistorique(authentication.getName()));
    }

    @PostMapping("/achat")
    public ResponseEntity<Void> acheter(Authentication authentication, @RequestBody @Valid TransactionActionReqDto req) throws InsufficientFundsException, NotFoundException, InterruptedException {
        portefeuilleService.acheterAction(authentication.getName(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vente")
    public ResponseEntity<Void> vendre(Authentication authentication, @RequestBody @Valid TransactionActionReqDto req) throws NotEnoughStocksException, NotFoundException, InterruptedException {
        portefeuilleService.vendreAction(authentication.getName(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favori")
    public ResponseEntity<Void> ajouterFavori(Authentication authentication, @RequestBody @Valid TickerReqDto req) throws TooManyFavorites {
        portefeuilleService.ajouterFavori(req.getTicker(), authentication.getName());
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest().build().toUri()
                        .resolve(String.format("favori/%s", req.getTicker()))
        ).build();
    }

    @DeleteMapping("/favori")
    public ResponseEntity<Void> supprimerFavori(Authentication authentication, @RequestBody @Valid TickerReqDto req) {
        portefeuilleService.supprimerFavori(req.getTicker(), authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardDto> getLeaderboard(String username) {
        return ResponseEntity.ok(rankService.getLeaderboard(username));
    }

    @GetMapping("/stock/{ticker}")
    public ResponseEntity<StockPerformanceDto> getStockPerformance(Authentication authentication, @PathVariable String ticker) throws InterruptedException, NotFoundException {
        return ResponseEntity.ok(portefeuilleService.getStockPerformance(ticker, authentication.getName()));
    }
}

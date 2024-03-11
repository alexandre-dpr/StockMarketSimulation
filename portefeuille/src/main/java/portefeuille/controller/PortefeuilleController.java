package portefeuille.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.LeaderboardDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.request.FavoriteReqDto;
import portefeuille.dto.request.TransactionActionReqDto;
import portefeuille.dto.request.UsernameReqDto;
import portefeuille.exceptions.*;
import portefeuille.service.PortefeuilleService;
import portefeuille.service.RankService;

import java.util.List;

@RestController
@RequestMapping("/portefeuille")
public class PortefeuilleController {

    @Autowired
    private PortefeuilleService portefeuilleService;

    @Autowired
    private RankService rankService;

    // TODO Une fois Spring Sec rajouté, ne plus prendre le username dans rq body mais dans Principal
    @PostMapping
    public ResponseEntity<PortefeuilleDto> createPortefeuille(@RequestBody @Valid UsernameReqDto req) throws WalletAlreadyCreatedException {
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest().build().toUri()
                        .resolve(String.format("/%s", req.getUsername()))
        ).body(portefeuilleService.creerPortefeuille(req.getUsername()));
    }

    @GetMapping
    public ResponseEntity<PortefeuilleDto> getPortefeuille(@RequestBody @Valid UsernameReqDto req) throws NotFoundException, InterruptedException {
        return ResponseEntity.ok().body(portefeuilleService.getPortefeuilleDto(req.getUsername()));
    }

    @GetMapping("/historique")
    public ResponseEntity<HistoryDto> getHistorique(@RequestBody @Valid UsernameReqDto req) throws NotFoundException {
        return ResponseEntity.ok().body(portefeuilleService.getHistorique(req.getUsername()));
    }

    @PostMapping("/achat")
    public ResponseEntity<Void> acheter(@RequestBody @Valid TransactionActionReqDto req) throws InsufficientFundsException, NotFoundException, InterruptedException {
        portefeuilleService.acheterAction(req.getUsername(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vente")
    public ResponseEntity<Void> vendre(@RequestBody @Valid TransactionActionReqDto req) throws NotEnoughStocksException, NotFoundException, InterruptedException {
        portefeuilleService.vendreAction(req.getUsername(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favori")
    public ResponseEntity<List<String>> getFavoris(@RequestBody @Valid UsernameReqDto req) {
        return ResponseEntity.ok(portefeuilleService.getFavoris(req.getUsername()));
    }

    @PostMapping("/favori")
    public ResponseEntity<Void> ajouterFavori(@RequestBody @Valid FavoriteReqDto req) throws TooManyFavorites {
        portefeuilleService.ajouterFavori(req.getTicker(), req.getUsername());
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest().build().toUri()
                        .resolve(String.format("favori/%s", req.getTicker()))
        ).build();
    }

    @DeleteMapping("/favori")
    public ResponseEntity<Void> supprimerFavori(@RequestBody @Valid FavoriteReqDto req) {
        portefeuilleService.supprimerFavori(req.getTicker(), req.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardDto> getLeaderboard(@RequestBody @Valid UsernameReqDto req) {
        return ResponseEntity.ok(rankService.getLeaderboard(req.getUsername()));
    }
}

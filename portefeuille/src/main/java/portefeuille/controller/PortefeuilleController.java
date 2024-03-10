package portefeuille.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.request.FavoriteReqDto;
import portefeuille.dto.request.TransactionActionReqDto;
import portefeuille.dto.request.UsernameReqDto;
import portefeuille.exceptions.*;
import portefeuille.service.PortefeuilleService;

import java.util.List;

@RestController
@RequestMapping("/portefeuille")
public class PortefeuilleController {

    @Autowired
    PortefeuilleService portefeuilleService;

    // TODO Une fois Spring Sec rajouté, ne plus prendre le username dans rq body mais dans Principal
    @PostMapping
    public ResponseEntity<PortefeuilleDto> createPortefeuille(Authentication authentication) throws WalletAlreadyCreatedException {
        String username = authentication.getName();
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest().build().toUri()
                        .resolve(String.format("/%s", username))
        ).body(portefeuilleService.creerPortefeuille(username));
    }

    @GetMapping
    public ResponseEntity<PortefeuilleDto> getPortefeuille(Authentication authentication) throws NotFoundException, InterruptedException {
        return ResponseEntity.ok().body(portefeuilleService.getPortefeuille(authentication.getName()));
    }

    @GetMapping("/historique")
    public ResponseEntity<HistoryDto> getHistorique(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok().body(portefeuilleService.getHistorique(authentication.getName()));
    }

    @PostMapping("/achat")
    public ResponseEntity<Void> acheter(Authentication authentication,@RequestBody @Valid TransactionActionReqDto req) throws InsufficientFundsException, NotFoundException, InterruptedException {
        portefeuilleService.acheterAction(authentication.getName(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vente")
    public ResponseEntity<Void> vendre(Authentication authentication, @RequestBody @Valid TransactionActionReqDto req) throws NotEnoughStocksException, NotFoundException, InterruptedException {
        portefeuilleService.vendreAction(authentication.getName(), req.getTicker(), req.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favori")
    public ResponseEntity<List<String>> getFavoris(Authentication authentication) {
        return ResponseEntity.ok(portefeuilleService.getFavoris(authentication.getName()));
    }

    @PostMapping("/favori")
    public ResponseEntity<Void> ajouterFavori(Authentication authentication,@RequestBody @Valid FavoriteReqDto req) throws TooManyFavorites {
        portefeuilleService.ajouterFavori(req.getTicker(), authentication.getName());
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest().build().toUri()
                        .resolve(String.format("favori/%s", req.getTicker()))
        ).build();
    }

    @DeleteMapping("/favori")
    public ResponseEntity<Void> supprimerFavori(Authentication authentication, @RequestBody @Valid FavoriteReqDto req) {
        portefeuilleService.supprimerFavori(req.getTicker(), authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

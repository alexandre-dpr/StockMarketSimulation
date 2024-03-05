package portefeuille.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import portefeuille.dto.HistoryDto;
import portefeuille.dto.PortefeuilleDto;
import portefeuille.dto.request.TransactionActionReqDto;
import portefeuille.dto.request.UsernameReqDto;
import portefeuille.exceptions.InsufficientFundsException;
import portefeuille.exceptions.NotEnoughStocksException;
import portefeuille.exceptions.NotFoundException;
import portefeuille.exceptions.WalletAlreadyCreatedException;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.service.PortefeuilleService;

@RestController
@RequestMapping("/portefeuille")
public class PortefeuilleController {
    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    PortefeuilleService portefeuilleService;

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
    public ResponseEntity<PortefeuilleDto> getPortefeuille(@RequestBody @Valid UsernameReqDto req) throws NotFoundException {
        return ResponseEntity.ok().body(portefeuilleService.getPortefeuille(req.getUsername()));
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
}

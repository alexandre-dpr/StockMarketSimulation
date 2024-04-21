package portefeuille.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface IPriceService {

    /**
     * Retourne le prix d'une action
     *
     * @param ticker le nom de l'action
     * @return le prix de l'action
     * @throws InterruptedException si l'envoi du message est interrompu
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    double getPrice(String ticker) throws InterruptedException;
}

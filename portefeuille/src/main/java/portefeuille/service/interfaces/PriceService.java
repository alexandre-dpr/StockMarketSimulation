package portefeuille.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface PriceService {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    double getPrice(String ticker) throws InterruptedException;
}

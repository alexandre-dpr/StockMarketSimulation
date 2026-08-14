package portefeuille.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.repository.TickerInfoRepository;
import portefeuille.service.IPriceService;


@Service
@RequiredArgsConstructor
public class DirectPriceService implements IPriceService {

    private final TickerInfoRepository tickerInfoRepository;

    private final RabbitMqSender sender;

    @Override
    public double getPrice(String ticker) {
        Double price = sender.send(ticker);
        if (price == null) {
            throw new RuntimeException("RabbitMQ timeout or error getting price for " + ticker);
        }
        return price;
    }
}

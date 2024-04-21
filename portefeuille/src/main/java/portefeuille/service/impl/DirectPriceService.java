package portefeuille.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.repository.TickerInfoRepository;
import portefeuille.service.IPriceService;


@Service
public class DirectPriceService implements IPriceService {

    @Autowired
    TickerInfoRepository tickerInfoRepository;

    @Autowired
    RabbitMqSender sender;

    @Override
    public double getPrice(String ticker) {
        return sender.send(ticker);
    }
}

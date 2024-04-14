package portefeuille.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portefeuille.dto.rabbitMq.TickerInfoDto;
import portefeuille.rabbitmq.RabbitMqSender;
import portefeuille.repository.TickerInfoRepository;
import portefeuille.service.interfaces.PriceService;

import java.util.UUID;


@Service
public class DirectPriceService implements PriceService {

    @Autowired
    TickerInfoRepository tickerInfoRepository;

    @Autowired
    RabbitMqSender sender;

    @Override
    public double getPrice(String ticker) throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        sender.send(new TickerInfoDto(uuid, ticker, null));
        while (tickerInfoRepository.findById(uuid).isEmpty()) {
            Thread.sleep(75);
        }
        double price = tickerInfoRepository.findById(uuid).get().getPrice();
        tickerInfoRepository.deleteById(uuid);
        return price;
    }
}

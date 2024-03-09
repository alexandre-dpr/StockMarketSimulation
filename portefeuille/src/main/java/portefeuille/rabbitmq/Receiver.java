package portefeuille.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import portefeuille.dto.rabbitMq.TickerInfoDto;
import portefeuille.modele.TickerInfo;
import portefeuille.repository.TickerInfoRepository;
import portefeuille.service.CacheService;

@Component
public class Receiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    CacheService cacheService;
    @Autowired
    TickerInfoRepository repository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(TickerInfoDto ticker) {
        logger.info("Service portfeuille TickerReceived is  " + ticker);
        //       cacheService.addTickerInfo(ticker);
        repository.save(TickerInfo.builder().uuid(ticker.uuid()).price(ticker.price()).ticker(ticker.ticker()).build());
        logger.info("Service portfeuille Ticker saved  " + ticker);
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}

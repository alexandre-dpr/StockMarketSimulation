package bourse.rabbitMq;

import bourse.dto.StockDto;
import bourse.dto.rabbitMq.TickerInfo;
import bourse.enums.Range;
import bourse.exceptions.UnauthorizedException;
import bourse.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Receiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    private StockService stockService;
    @Autowired
    RabbitMqSender rabbitMqSender ;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(TickerInfo ticker) throws UnauthorizedException, IOException {
        logger.info("TickerReceived is  " + ticker);
        StockDto stock = stockService.getStock(ticker.ticker(), Range.ONE_DAY);
        rabbitMqSender.send(new TickerInfo(ticker.uuid(), ticker.ticker(),stock.getPrice()));
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}

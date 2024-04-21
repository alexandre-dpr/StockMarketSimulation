package bourse.rabbitmq;

import bourse.dto.StockDto;
import bourse.dto.rabbitMq.TickerInfo;
import bourse.enums.Range;
import bourse.exceptions.NotFoundException;
import bourse.exceptions.UnauthorizedException;
import bourse.service.impl.StockService;
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



    @RabbitListener(queues = "${spring.rabbitmq.queue.price}")
    public Double receivedMessage(TickerInfo ticker) throws UnauthorizedException, IOException, NotFoundException {
        logger.info("TickerReceived is  " + ticker);
        StockDto stock = stockService.getStock(ticker.ticker(), Range.ONE_DAY);
        return stock.getPrice();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}

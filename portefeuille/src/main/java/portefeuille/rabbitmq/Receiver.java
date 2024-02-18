package portefeuille.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;
import portefeuille.dto.rabbitMq.TickerInfo;

@Component
public class Receiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(TickerInfo ticker) {

        logger.info("TickerReceived is  " + ticker);
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}

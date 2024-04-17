package portefeuille.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import portefeuille.dto.rabbitMq.OrderDto;
import portefeuille.exceptions.InsufficientFundsException;
import portefeuille.exceptions.NotEnoughStocksException;
import portefeuille.exceptions.NotFoundException;
import portefeuille.repository.TickerInfoRepository;
import portefeuille.service.PortefeuilleService;

@Component
public class Receiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    TickerInfoRepository repository;

    @Autowired
    PortefeuilleService portefeuilleService;


    @RabbitListener(queues = "${spring.rabbitmq.queue.action}")
    public void receivedAction(OrderDto order) throws InsufficientFundsException, NotFoundException, InterruptedException, NotEnoughStocksException {
        logger.info("Service portfeuille Order received is  " + order);
        if (order.action().equalsIgnoreCase("achat")){
            portefeuilleService.acheterAction(order.username(), order.ticker(), order.quantity());
        }else {
            portefeuilleService.vendreAction(order.username(), order.ticker(), order.quantity());
        }
        logger.info("Service portfeuille Order executed  " + order);
    }
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }
}

package com.github.wirecard.consumer;

import com.github.wirecard.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CardCreditMensageConsumer {

    @RabbitListener(queues = RabbitMQConfig.CARD_NAME)
    public void receiveMessage(String message) {
        log.info("Received Message: {}", message);
    }
}

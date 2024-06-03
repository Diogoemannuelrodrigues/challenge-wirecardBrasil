package com.github.wirecard.producer;

import com.github.wirecard.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardCreditMensageProducer {


    private final RabbitTemplate rabbitTemplate;

    public void sendMensage(String cardCreditmensage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CARD_NAME, "wirecardbrasil.cardCredit", cardCreditmensage);
        log.info("Sent email message: {}", cardCreditmensage);
    }
}

package com.github.wirecard.producer;

import com.github.wirecard.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmail(String emailMessage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "wirecardbrasil.email", emailMessage);
        log.info("Sent email message: {}", emailMessage);
    }
}

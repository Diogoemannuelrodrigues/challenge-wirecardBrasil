package br.com.github.wirecardBrasil.consumer;

import br.com.github.wirecardBrasil.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE_NAME)
    public void receiveEmailMessage(String emailMessage) {
        log.info("Received email message: {}", emailMessage);
    }
}

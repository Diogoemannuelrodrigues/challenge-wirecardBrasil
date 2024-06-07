package com.github.wirecard.producer;

import com.github.wirecard.config.RabbitMQConfig;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.record.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientMensageProducer {


    private final RabbitTemplate rabbitTemplate;

    public void sendObjectForGenerateCardCredit(@Payload Client client) {
        log.info("Send client for generate cardCredit:", client);
        var response = ClientResponse.builder().name(client.getName()).id(client.getId()).build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "wirecardbrasil.client", response);
    }
}


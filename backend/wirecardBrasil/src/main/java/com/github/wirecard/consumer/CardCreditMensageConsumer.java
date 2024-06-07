package com.github.wirecard.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wirecard.config.RabbitMQConfig;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.record.CardCreditRequest;
import com.github.wirecard.entidade.record.ClientRequest;
import com.github.wirecard.entidade.record.ClientResponse;
import com.github.wirecard.exceptions.ClientNotFoundException;
import com.github.wirecard.service.CardCreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class CardCreditMensageConsumer {

    private final CardCreditService cardCreditService;

    @RabbitListener(queues = RabbitMQConfig.CARD_NAME)
    public void receiveMessage(String message) {
        log.info("Received Message: {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.CLIENT_NAME)
    public void receiveObject(@Payload String clientJson) {
        log.info("Received client {}", clientJson);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = objectMapper.readValue(clientJson, ClientResponse.class);
            var cardRequest = CardCreditRequest.builder()
                    .clientName(clientResponse.name())
                    .clientId(clientResponse.id())
                    .build();
            cardCreditService.genereteCardCerdit(cardRequest);
        } catch (JsonProcessingException e) {
            log.error("Error processing client message: {}", e.getMessage());
        }
    }
}

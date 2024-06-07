package com.github.wirecard.service;

import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.record.ClientRequest;
import com.github.wirecard.entidade.record.ClientResponse;
import com.github.wirecard.producer.ClientMensageProducer;
import com.github.wirecard.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMensageProducer clientProducer;

    public ClientResponse create(ClientRequest clientRequest) {
        if (clientRequest == null || isEmpty(clientRequest)){
            throw new IllegalArgumentException("Client request cannot be null or empty");
        }
        var client = clientRequest.toEntity();
        client = clientRepository.save(client);
        log.info("Enviando mesnasgem para o producer.....");
        clientProducer.sendObjectForGenerateCardCredit(client);
        return ClientResponse.fromEntity(client);
    }

    public ClientResponse findClient(UUID id) {
        return clientRepository.findById(id).map(ClientResponse::fromEntity).orElseThrow(IllegalArgumentException::new);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}

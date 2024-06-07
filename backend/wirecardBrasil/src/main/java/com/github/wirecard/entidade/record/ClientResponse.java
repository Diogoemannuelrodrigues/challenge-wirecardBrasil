package com.github.wirecard.entidade.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ClientResponse(UUID id,
                             String name,
                             String cpf,
                             Integer age,
                             BigDecimal salary,
                             Set<CardCredit> cardCredits
                             ) {

    public static ClientResponse fromEntity(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getAge(),
                client.getSalary(),
                client.getCardCredits()
        );
    }

    public static Client toEntity(ClientResponse response) {
        return Client.builder()
                .id(response.id)
                .name(response.name())
                .cpf(response.cpf())
                .age(response.age())
                .salary(response.salary)
                .cardCredits(response.cardCredits)
                .build();
    }

}



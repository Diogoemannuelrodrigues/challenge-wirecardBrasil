package com.github.wirecard.entidade.record;

import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;

@Builder
public record ClientRequest(String name,
                            String cpf,
                            Integer age,
                            BigDecimal salary,
                            Set<CardCredit> cardCredits) {

    public Client toEntity(){
        return Client
                .builder()
                .salary(salary)
                .name(name)
                .cpf(cpf)
                .cardCredits(cardCredits)
                .age(age)
                .build();
    }
}

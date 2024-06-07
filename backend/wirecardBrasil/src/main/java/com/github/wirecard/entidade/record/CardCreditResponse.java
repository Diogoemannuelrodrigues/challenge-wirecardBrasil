package com.github.wirecard.entidade.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.wirecard.entidade.Account;
import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.Enum.CardBrandEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardCreditResponse(UUID id,
                                 String holderName,
                                 String number,
                                 LocalDate expirationDate,
                                 BigDecimal limit,
                                 Client client,
                                 CardBrandEnum cardBrand,
                                 Set<Account> accounts) {

    public static CardCreditResponse toEntityResponse(CardCredit credit) {
        return new CardCreditResponse(
                credit.getId(),
                credit.getHolderName(),
                credit.getNumber(),
                credit.getExpirationDate(),
                credit.getLimit(),
                credit.getClient(),
                credit.getCardBrand(),
                credit.getAccounts()
        );
    }
    public CardCredit toEntity() {
        return CardCredit.builder()
                .id(this.id)
                .holderName(this.holderName)
                .number(this.number)
                .expirationDate(this.expirationDate)
                .limit(this.limit)
                .client(this.client)
                .cardBrand(this.cardBrand)
                .accounts(this.accounts)
                .build();
    }
}




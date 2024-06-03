package com.github.wirecard.entidade.record;

import com.github.wirecard.entidade.Boleto;
import com.github.wirecard.entidade.Buyer;
import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.Enum.EstadoPagamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.wirecard.entidade.Enum.TypePaymentEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PaymentRecordResponse(UUID id,
                                    CardCredit cardCredit,
                                    Client client,
                                    Buyer buyer,
                                    BigDecimal amount,
                                    TypePaymentEnum typePayment,
                                    EstadoPagamento estadoPagamento,
                                    Boleto boleto ) {
}

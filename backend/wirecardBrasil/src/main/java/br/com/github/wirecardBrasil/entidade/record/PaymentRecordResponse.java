package br.com.github.wirecardBrasil.entidade.record;

import br.com.github.wirecardBrasil.entidade.Boleto;
import br.com.github.wirecardBrasil.entidade.Buyer;
import br.com.github.wirecardBrasil.entidade.CardCredit;
import br.com.github.wirecardBrasil.entidade.Client;
import br.com.github.wirecardBrasil.entidade.Enum.EstadoPagamento;
import br.com.github.wirecardBrasil.entidade.Enum.TypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
                                    TypeEnum typeEnum,
                                    EstadoPagamento estadoPagamento,
                                    Boleto boleto ) {
}

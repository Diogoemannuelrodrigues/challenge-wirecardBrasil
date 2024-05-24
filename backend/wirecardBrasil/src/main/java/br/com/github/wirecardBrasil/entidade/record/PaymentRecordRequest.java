package br.com.github.wirecardBrasil.entidade.record;

import br.com.github.wirecardBrasil.entidade.Boleto;
import br.com.github.wirecardBrasil.entidade.Buyer;
import br.com.github.wirecardBrasil.entidade.CardCredit;
import br.com.github.wirecardBrasil.entidade.Client;
import br.com.github.wirecardBrasil.entidade.Enum.TypeEnum;

import java.math.BigDecimal;

public record PaymentRecordRequest(CardCredit cardCredit,
                                   Client client,
                                   Buyer buyer,
                                   BigDecimal amount,
                                   TypeEnum typeEnum,
                                   Boleto boleto) {
}

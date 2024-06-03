package com.github.wirecard.entidade.record;

import com.github.wirecard.entidade.Boleto;
import com.github.wirecard.entidade.Buyer;
import com.github.wirecard.entidade.Enum.TypePaymentEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRecordRequest(UUID IdClient,
                                   Buyer buyer,
                                   BigDecimal amount,
                                   TypePaymentEnum typePayment,
                                   Boleto boleto) {
}

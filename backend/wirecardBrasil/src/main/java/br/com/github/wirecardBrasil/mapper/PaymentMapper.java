package br.com.github.wirecardBrasil.mapper;

import br.com.github.wirecardBrasil.entidade.Payment;
import br.com.github.wirecardBrasil.entidade.record.PaymentRecordResponse;

public class PaymentMapper {

    public static PaymentRecordResponse toPaymentRecordResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentRecordResponse.builder()
                .id(payment.getId())
                .buyer(payment.getBuyer())
                .amount(payment.getAmount())
                .boleto(payment.getBoleto())
                .cardCredit(payment.getCardCredit())
                .client(payment.getClient())
                .estadoPagamento(payment.getEstadoPagamento())
                .typeEnum(payment.getType())
                .build();
    }
}

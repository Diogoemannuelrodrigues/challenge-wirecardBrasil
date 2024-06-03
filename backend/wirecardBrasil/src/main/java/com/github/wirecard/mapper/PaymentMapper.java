package com.github.wirecard.mapper;

import com.github.wirecard.entidade.Payment;
import com.github.wirecard.entidade.record.PaymentRecordResponse;

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
                .client(payment.getClient())
                .estadoPagamento(payment.getEstadoPagamento())
                .typePayment(payment.getTypePayment())
                .build();
    }
}

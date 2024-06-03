package com.github.wirecard.entidade.record;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BoletoPaymentResponse(String codigoDeBarra, BigDecimal valor, LocalDate vencimento) {
}

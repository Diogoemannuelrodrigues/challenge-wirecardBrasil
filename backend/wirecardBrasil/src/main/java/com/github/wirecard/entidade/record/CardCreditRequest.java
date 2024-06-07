package com.github.wirecard.entidade.record;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CardCreditRequest(UUID clientId, String clientName) {
}

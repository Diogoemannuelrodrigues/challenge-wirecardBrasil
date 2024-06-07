package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wirecard.entidade.Enum.StatusBuye;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class EventCardCreditHistory {

    private LocalDateTime createdAt;
    private StatusBuye status;
    private String mensage;

}

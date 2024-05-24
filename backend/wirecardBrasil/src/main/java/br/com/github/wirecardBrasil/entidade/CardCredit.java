package br.com.github.wirecardBrasil.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
@JsonIgnoreProperties
public class CardCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeTitular;

    private String numero;

    private String dataExpiracao;

    private String cvv;

    @Column(name = "credit_limit")
    private BigDecimal limit;

    public CardCredit(String name, String s, LocalDate localDate, String s1, BigDecimal bigDecimal) {
    }
}

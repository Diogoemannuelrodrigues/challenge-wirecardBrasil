package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
@JsonIgnoreProperties
public class CardCredit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String holderName;

    private String number;

    private String expirationDate;

    private String cvv;

    @Column(name = "credit_limit")
    private BigDecimal limit;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "cardCredit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Account> accounts;

    public CardCredit(String name, String s, LocalDate localDate, String s1, BigDecimal bigDecimal) {
    }
}

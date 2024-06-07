package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wirecard.entidade.Enum.CardBrandEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
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

    private LocalDate expirationDate;

    private LocalDate createdAt;

    private String cvv;

    @Enumerated(EnumType.STRING)
    private CardBrandEnum cardBrand;

    @Column(name = "credit_limit")
    private BigDecimal limit;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    @ManyToMany(mappedBy = "cardCredits")
    private Set<Account> accounts = new HashSet<>();

}

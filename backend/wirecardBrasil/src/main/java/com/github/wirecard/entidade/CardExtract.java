package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card_extract")
@JsonIgnoreProperties
public class CardExtract {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "cardExtract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions;
}

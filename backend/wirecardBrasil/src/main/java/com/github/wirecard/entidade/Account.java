package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "account")
@JsonIgnoreProperties
public class Account {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions = new HashSet<>();

    private Integer referenceMonth;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CardExtract> cardExtracts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "card_credit_id", nullable = false)
    private CardCredit cardCredit;

    public Account() {
            this.transactions = new HashSet<>();
    }

    public void adicionarTransacao(Transaction transacao) {
        transactions.add(transacao);
    }

    public double calcularTotal() {
        return transactions.stream().mapToDouble(Transaction::getValue).sum();
    }

    public Set<Transaction> getTransacoes() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "transacoes=" + transactions +
                '}';
    }
}


package com.github.wirecard.entidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wirecard.entidade.Enum.TypePaymentEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
@JsonIgnoreProperties
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID transactionId;

    private String description;

    private double value;

    @Immutable
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TypePaymentEnum typePayment;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "card_extract_id", nullable = false)
    private CardExtract cardExtract;

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
    }

}

package com.github.wirecard.entidade;

import com.github.wirecard.entidade.Enum.EstadoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wirecard.entidade.Enum.TypePaymentEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
@JsonIgnoreProperties
public class Payment {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    private BigDecimal amount;

    private TypePaymentEnum typePayment;

    @Enumerated(EnumType.STRING)
    private EstadoPagamento estadoPagamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "boleto_id", referencedColumnName = "id")
    private Boleto boleto;

}

package br.com.github.wirecardBrasil.entidade;

import br.com.github.wirecardBrasil.entidade.Enum.EstadoPagamento;
import br.com.github.wirecardBrasil.entidade.Enum.TypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cardCredit_id", referencedColumnName = "id")
    private CardCredit cardCredit;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    private BigDecimal amount;

    private TypeEnum type;

    @Enumerated(EnumType.STRING)
    private EstadoPagamento estadoPagamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "boleto_id", referencedColumnName = "id")
    private Boleto boleto;

}

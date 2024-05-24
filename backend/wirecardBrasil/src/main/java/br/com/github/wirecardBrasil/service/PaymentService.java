package br.com.github.wirecardBrasil.service;

import br.com.github.wirecardBrasil.entidade.*;
import br.com.github.wirecardBrasil.entidade.Enum.EstadoPagamento;
import br.com.github.wirecardBrasil.entidade.Enum.TypeEnum;
import br.com.github.wirecardBrasil.entidade.record.BoletoPaymentResponse;
import br.com.github.wirecardBrasil.entidade.record.PaymentRecordRequest;
import br.com.github.wirecardBrasil.entidade.record.PaymentRecordResponse;
import br.com.github.wirecardBrasil.exceptions.ClientNotFoundException;
import br.com.github.wirecardBrasil.exceptions.PaymentNotFounException;
import br.com.github.wirecardBrasil.exceptions.UpdateCardCreditException;
import br.com.github.wirecardBrasil.mapper.GerarCartaoCredito;
import br.com.github.wirecardBrasil.mapper.PaymentMapper;
import br.com.github.wirecardBrasil.repository.BoletoRepository;
import br.com.github.wirecardBrasil.repository.CardCreditRepository;
import br.com.github.wirecardBrasil.repository.ClientRepository;
import br.com.github.wirecardBrasil.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static br.com.github.wirecardBrasil.config.BarcodeGenerator.gerarCodigoBarras;
import static br.com.github.wirecardBrasil.config.BarcodeGenerator.trimEspacos;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardCreditRepository cardCreditRepository;
    private final BoletoRepository boletoRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public BoletoPaymentResponse generateTicket(String dataVencimento, String valor, String nossoNumero, String valorTotal) {
        Boleto boleto = new Boleto();
        var codigoDeBarras = gerarCodigoBarras(dataVencimento, valor, nossoNumero, valorTotal);
        boleto.setCodigoDeBarra(trimEspacos(codigoDeBarras));
        boleto.setValor(BigDecimal.valueOf(Long.parseLong(valor)));
        boleto.setVencimento(LocalDate.parse(dataVencimento));
        boletoRepository.save(boleto);

        return BoletoPaymentResponse.builder().codigoDeBarra(boleto.getCodigoDeBarra()).valor(boleto.getValor()).vencimento(boleto.getVencimento()).build();
    }

    public PaymentRecordResponse statusPayment(UUID id) {
        return paymentRepository.findById(id).map(PaymentMapper::toPaymentRecordResponse).orElseThrow(PaymentNotFounException::new);
    }

    public boolean isCreditLimitSufficient(CardCredit credit, BigDecimal decimal) {
        return credit.getLimit().compareTo(decimal) >= 0;
    }

    @Transactional
    public PaymentRecordResponse payment(PaymentRecordRequest request) {
        if (request.typeEnum().equals(TypeEnum.CARTAO_DE_CREDITO)){
            var client = clientRepository
                    .findById(1L)
                    .orElseThrow(ClientNotFoundException::new);

            var buyer = Buyer.builder()
                    .cpf("12345678900")
                    .email("Teste@gmail.com")
                    .id(1L)
                    .nome("Buyer teste")
                    .build();
            var payment = getPayment(request);

            payment.setBuyer(buyer);
            payment.setClient(client);

            payment.setCardCredit(genereteCardCerdit(payment.getClient().getNome(), BigDecimal.valueOf(3000)));
            payment.setEstadoPagamento(EstadoPagamento.PENDENTE);
            isCreditLimitSufficient(payment.getCardCredit(), request.amount());
            updateCardCreditLimit(payment);

            var result = paymentRepository.save(payment);
            return PaymentMapper.toPaymentRecordResponse(result);
        }
        throw new IllegalArgumentException("Type difent");
    }

    private static Payment getPayment(PaymentRecordRequest request) {
        return Payment.builder().type(request.typeEnum()).buyer(request.buyer()).cardCredit(request.cardCredit()).boleto(request.boleto()).amount(request.amount()).build();
    }

    private CardCredit genereteCardCerdit(String nome, BigDecimal limit){
        var cardCredit = CardCredit.builder()
                .nomeTitular(nome)
                .numero(GerarCartaoCredito.generateCreditCardNumber())
                .cvv(GerarCartaoCredito.geraNumeroCvv())
                .dataExpiracao(String.valueOf(GerarCartaoCredito.generateRandomFutureDate()))
                .limit(limit)
                .build();

        return cardCreditRepository.save(cardCredit);
    }

    private void updateCardCreditLimit(Payment request) {
        Optional<CardCredit> cardCreditOptional = cardCreditRepository.findById(request.getCardCredit().getId());

        cardCreditOptional.ifPresent(cardCredit -> {
            BigDecimal newLimit = cardCredit.getLimit().subtract(request.getAmount());
            if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
                throw new UpdateCardCreditException();
            }
            cardCredit.setLimit(newLimit);
            //TODO - todo Adicionar rabbitMe que tem um novo limite para o cartao!
            cardCreditRepository.save(cardCredit);
        });
    }

    @Transactional
    public PaymentRecordResponse checkout(UUID id) {
        var payment = paymentRepository.findById(id).orElseThrow(PaymentNotFounException::new);
        payment.setEstadoPagamento(EstadoPagamento.PAGO);
        //TODO - CHAMAR RABBIMQ

        var card = cardCreditRepository.findById(payment.getCardCredit().getId()).orElseThrow(IllegalArgumentException::new);
        BigDecimal newLimit = card.getLimit().add(payment.getAmount());
        card.setLimit(newLimit);

        paymentRepository.save(payment);
        CompletableFuture.runAsync(() -> cardCreditRepository.save(card))
                .exceptionally(ex -> {
                    log.info("Falha ao salvar o cartão: " + ex.getMessage());
                    return null;
                });
        return PaymentMapper.toPaymentRecordResponse(payment);
    }
}

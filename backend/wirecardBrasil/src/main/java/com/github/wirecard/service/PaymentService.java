package com.github.wirecard.service;

import com.github.wirecard.entidade.Boleto;
import com.github.wirecard.entidade.Buyer;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.Enum.EstadoPagamento;
import com.github.wirecard.entidade.Enum.TypePaymentEnum;
import com.github.wirecard.entidade.Payment;
import com.github.wirecard.entidade.record.BoletoPaymentResponse;
import com.github.wirecard.entidade.record.PaymentRecordRequest;
import com.github.wirecard.entidade.record.PaymentRecordResponse;
import com.github.wirecard.exceptions.BuyerNotFoundException;
import com.github.wirecard.exceptions.ClientNotFoundException;
import com.github.wirecard.exceptions.PaymentNotFounException;
import com.github.wirecard.mapper.PaymentMapper;
import com.github.wirecard.producer.EmailMessageProducer;
import com.github.wirecard.producer.PaymentMessageProducer;
import com.github.wirecard.repository.BoletoRepository;
import com.github.wirecard.repository.BuyerRepository;
import com.github.wirecard.repository.ClientRepository;
import com.github.wirecard.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.github.wirecard.config.BarcodeGenerator.gerarCodigoBarras;
import static com.github.wirecard.config.BarcodeGenerator.trimEspacos;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BoletoRepository boletoRepository;
    private final ClientRepository clientRepository;
    private final PaymentMessageProducer paymentMessageProducer;
    private final EmailMessageProducer emailMessageProducer;
    private final BuyerRepository buyerRepository;

    @Transactional
    public BoletoPaymentResponse generateTicket(String dataVencimento, String valor, String nossoNumero, String valorTotal) {
        Boleto boleto = new Boleto();
        var codigoDeBarras = gerarCodigoBarras(dataVencimento, valor, nossoNumero, valorTotal);
        boleto.setCodigoDeBarra(trimEspacos(codigoDeBarras));
        boleto.setValor(BigDecimal.valueOf(Long.parseLong(valor)));
        boleto.setVencimento(LocalDate.parse(dataVencimento));
        boletoRepository.save(boleto);
        paymentMessageProducer.sendMessage("Payment created: " + boleto);
        emailMessageProducer.sendEmail("Send email:" + boleto);
        return BoletoPaymentResponse.builder().codigoDeBarra(boleto.getCodigoDeBarra()).valor(boleto.getValor()).vencimento(boleto.getVencimento()).build();
    }

    public PaymentRecordResponse statusPayment(UUID id) {
        return paymentRepository.findById(id).map(PaymentMapper::toPaymentRecordResponse).orElseThrow(PaymentNotFounException::new);
    }



    @Transactional
    public PaymentRecordResponse payment(PaymentRecordRequest request) {
        if (request.typePayment().equals(TypePaymentEnum.CREDITO)){

            Client client = clientRepository.findById(request.IdClient()).orElseThrow(ClientNotFoundException::new);
            Buyer buyer = buyerRepository.findById(request.buyer().getId()).orElseThrow(BuyerNotFoundException::new);

            var payment = getPayment(request);

            payment.setBuyer(buyer);
            payment.setClient(client);

            payment.setEstadoPagamento(EstadoPagamento.PENDENTE);
/*
            isCreditLimitSufficient(payment.getCardCredit(), request.amount());
*/

            var result = paymentRepository.save(payment);
            paymentMessageProducer.sendMessage("Payment created: " + payment);
            emailMessageProducer.sendEmail("Send email:" + payment.getBuyer().getEmail());

            return PaymentMapper.toPaymentRecordResponse(result);
        }
        throw new IllegalArgumentException("Type difent");
    }

    private static Payment getPayment(PaymentRecordRequest request) {
        return Payment.builder().typePayment(request.typePayment()).buyer(request.buyer()).boleto(request.boleto()).amount(request.amount()).build();
    }

    @Transactional
    public PaymentRecordResponse checkout(UUID id) {
        var payment = paymentRepository
                .findById(id)
                .orElseThrow(PaymentNotFounException::new);

        payment.setEstadoPagamento(EstadoPagamento.PAGO);

//        cardCreditService.returnCreditForTheCard(payment.getAmount(), payment.getClient().getCardCredit().getId());

        paymentRepository.save(payment);
//        CompletableFuture.runAsync(() -> cardCreditRepository.save(card))
//                .exceptionally(ex -> {
//                    log.info("Falha ao salvar o cartão: " + ex.getMessage());
//                    return null;
//                });
        paymentMessageProducer.sendMessage("CardCredit update sucesses: " + payment);
        emailMessageProducer.sendEmail("Checkout with sucssess" + payment.getBuyer().getEmail());

        return PaymentMapper.toPaymentRecordResponse(payment);
    }
    public List<PaymentRecordResponse> allPayments() {
        return paymentRepository
                .findAll()
                .stream()
                .map(PaymentMapper::toPaymentRecordResponse)
                .toList();
    }
}

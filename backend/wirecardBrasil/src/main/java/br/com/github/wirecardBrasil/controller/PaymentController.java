package br.com.github.wirecardBrasil.controller;

import br.com.github.wirecardBrasil.entidade.record.BoletoPaymentResponse;
import br.com.github.wirecardBrasil.entidade.record.PaymentRecordRequest;
import br.com.github.wirecardBrasil.entidade.record.PaymentRecordResponse;
import br.com.github.wirecardBrasil.service.PaymentService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/credit-card")
    public ResponseEntity<PaymentRecordResponse> processCreditCardPayment(@RequestBody PaymentRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.payment(request));
    }

    @PostMapping("/ticket")
    public ResponseEntity<BoletoPaymentResponse> processTicketPayment(@RequestParam("dataVencimento") @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String dataVencimento,
                                                                      @RequestParam("valor") @NotBlank @Pattern(regexp = "\\d+(\\.\\d{1,2})?") String valor,
                                                                      @RequestParam("nossoNumero") @NotBlank @Size(min = 5, max = 15) String nossoNumero,
                                                                      @RequestParam("valorTotal") @NotNull @Positive BigDecimal valorTotal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.generateTicket(dataVencimento, valor, nossoNumero, String.valueOf(valorTotal)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentRecordResponse> statusPayment(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.statusPayment(id));
    }
    @GetMapping("/checkout/{id}")
    public ResponseEntity<PaymentRecordResponse> checkOut(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.checkout(id));
    }
}

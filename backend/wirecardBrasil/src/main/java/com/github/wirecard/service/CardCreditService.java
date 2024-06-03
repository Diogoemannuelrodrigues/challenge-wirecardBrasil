package com.github.wirecard.service;

import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.exceptions.ErrorCardException;
import com.github.wirecard.exceptions.UpdateCardCreditException;
import com.github.wirecard.producer.CardCreditMensageProducer;
import com.github.wirecard.repository.CardCreditRepository;
import com.github.wirecard.service.clients.ValidateCardCreditClient;
import com.github.wirecard.mapper.GerarCartaoCredito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardCreditService {

    private final CardCreditRepository cardCreditRepository;
    private final ValidateCardCreditClient validateCardCredit;
    private final CardCreditMensageProducer cardCreditMensageProducer;


    public CardCredit genereteCardCerdit(Client client){
        var cardCredit = CardCredit.builder()
                .holderName(client.getName())
                .number(GerarCartaoCredito.generateCreditCardNumber())
                .cvv(GerarCartaoCredito.geraNumeroCvv())
                .expirationDate(String.valueOf(GerarCartaoCredito.generateRandomFutureDate()))
                .limit(calculateCreditLimit(client))
                .build();

        var result = validateCardCredit.validate(cardCredit);

        if(!result.isEmpty()) {
            cardCreditMensageProducer.sendMensage("Card generetad and valited with sucssess" + cardCredit);
            return cardCreditRepository.save(cardCredit);
        }
        throw new ErrorCardException();
    }

    public void updateCardCreditLimit(UUID idCardCredit, BigDecimal request) {
        Optional<CardCredit> cardCreditOptional = cardCreditRepository.findById(idCardCredit);

        cardCreditOptional.ifPresent(cardCredit -> {
            BigDecimal newLimit = cardCredit.getLimit().subtract(request);
            if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
                throw new UpdateCardCreditException();
            }
            cardCredit.setLimit(newLimit);
            cardCreditMensageProducer.sendMensage("Card generetad with sucssess"+ cardCredit);
            cardCreditRepository.save(cardCredit);
        });
    }

    public boolean isCreditLimitSufficient(CardCredit credit, BigDecimal decimal) {
        return credit.getLimit().compareTo(decimal) >= 0;
    }

    public BigDecimal calculateCreditLimit(Client client) {
        final double CREDIT_SCORE_WEIGHT = 0.4;
        final double INCOME_WEIGHT = 0.3;
        final double JOB_STABILITY_WEIGHT = 0.1;
        final double PAYMENT_HISTORY_WEIGHT = 0.1;
        final double DEBT_LEVEL_WEIGHT = 0.05;
        final double CUSTOMER_LOYALTY_WEIGHT = 0.05;

        // Obter valores dos critérios
        double creditScore = getCreditScore();
        BigDecimal monthlyIncome = client.getSalary();
        int monthsInJob = 12;
        int latePayments = 500;
        BigDecimal totalDebt = new BigDecimal(500);
        int monthsAsCustomer = getMonthsAsCustomer();

        // Calcular o score para cada critério
        double creditScoreRating = creditScore * CREDIT_SCORE_WEIGHT;
        double incomeRating = monthlyIncome.doubleValue() * INCOME_WEIGHT;
        double jobStabilityRating = (monthsInJob / 12.0) * JOB_STABILITY_WEIGHT; // anos no emprego
        double paymentHistoryRating = (latePayments == 0 ? 1 : 0) * PAYMENT_HISTORY_WEIGHT;
        double debtLevelRating = (totalDebt.compareTo(BigDecimal.ZERO) == 0 ? 1 : 0) * DEBT_LEVEL_WEIGHT;
        double customerLoyaltyRating = (monthsAsCustomer / 12.0) * CUSTOMER_LOYALTY_WEIGHT; // anos como cliente

        double totalScore = creditScoreRating + incomeRating + jobStabilityRating + paymentHistoryRating + debtLevelRating + customerLoyaltyRating;

        BigDecimal creditLimit;
        if (totalScore >= 0.8) {
            creditLimit = monthlyIncome.multiply(new BigDecimal(5));
        } else if (totalScore >= 0.6) {
            creditLimit = monthlyIncome.multiply(new BigDecimal(3));
        } else if (totalScore >= 0.4) {
            creditLimit = monthlyIncome.multiply(new BigDecimal(1.5));
        } else {
            creditLimit = monthlyIncome.multiply(new BigDecimal(0.5));
        }

        return creditLimit;
    }

    private double getCreditScore() {
        var random = new Random();
        return random.nextInt(10);
    }

    private int getMonthsAsCustomer() {
        var random = new Random();
        return random.nextInt(12);
    }

    public CardCredit returnCreditForTheCard(BigDecimal amount, UUID idCardCredit) {
        return cardCreditRepository.findById(idCardCredit)
                .map(cardCredit -> {
            cardCredit.setLimit(amount);
            return cardCreditRepository.save(cardCredit);
        }).orElseThrow(ErrorCardException::new);
    }

    //todo - metodo para gerar o qrcode

    }

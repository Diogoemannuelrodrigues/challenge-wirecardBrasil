package com.github.wirecard.service;

import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Client;
import com.github.wirecard.entidade.Enum.CardBrandEnum;
import com.github.wirecard.entidade.record.CardCreditRequest;
import com.github.wirecard.entidade.record.CardCreditResponse;
import com.github.wirecard.entidade.record.ClientResponse;
import com.github.wirecard.exceptions.ErrorCardException;
import com.github.wirecard.exceptions.UpdateCardCreditException;
import com.github.wirecard.producer.CardCreditMensageProducer;
import com.github.wirecard.repository.CardCreditRepository;
import com.github.wirecard.repository.ClientRepository;
import com.github.wirecard.service.clients.ValidateCardCreditClient;
import com.github.wirecard.config.GerarCartaoCredito;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardCreditService {

    private final CardCreditRepository cardCreditRepository;
    private final ValidateCardCreditClient validateCardCredit;
    private final CardCreditMensageProducer cardCreditMensageProducer;
    private final ClientRepository clientRepository;

    @Transactional
    public CardCreditResponse genereteCardCerdit(CardCreditRequest creditRequest){

            var client = clientRepository.findById(creditRequest.clientId()).orElseThrow(IllegalArgumentException::new);
            var clientResponse = ClientResponse.fromEntity(client);
            var clientResponses = ClientResponse.toEntity(clientResponse);

            var cardCredit = CardCredit.builder()
                    .holderName(creditRequest.clientName())
                    .number(GerarCartaoCredito.generateCreditCardNumber())
                    .cvv(GerarCartaoCredito.geraNumeroCvv())
                    .expirationDate((GerarCartaoCredito.generateRandomFutureDate()))
                    .limit(calculateCreditLimit(clientResponses))
                    .build();

            var result = validateCardCredit.validate(cardCredit);

            cardCredit.setCardBrand(validateCardBrand(cardCredit));
            cardCredit.setCreatedAt(LocalDate.now());
            cardCredit.setClient(client);
            if(!result.isEmpty()) {
                var card = cardCreditRepository.save(cardCredit);
                cardCreditMensageProducer.sendMensage("CardCredir create with success " + cardCredit);
                return CardCreditResponse.toEntityResponse(card);
        }

        throw new ErrorCardException();
    }

    private CardBrandEnum validateCardBrand(CardCredit cardCredit) {

        var number = cardCredit.getNumber().charAt(0);

        return switch (number){
            case '4' -> CardBrandEnum.VISA;
            case '3' -> CardBrandEnum.AMEX;
            case '6' -> CardBrandEnum.DISCOVER;
            case '5' -> CardBrandEnum.MASTERCARD;
            default -> CardBrandEnum.UNKNOWN;
        };

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
        double jobStabilityRating = (monthsInJob / 12.0) * JOB_STABILITY_WEIGHT;
        double paymentHistoryRating = (latePayments == 0 ? 1 : 0) * PAYMENT_HISTORY_WEIGHT;
        double debtLevelRating = (totalDebt.compareTo(BigDecimal.ZERO) == 0 ? 1 : 0) * DEBT_LEVEL_WEIGHT;
        double customerLoyaltyRating = (monthsAsCustomer / 12.0) * CUSTOMER_LOYALTY_WEIGHT;

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

    public void destroyCardCredit(UUID id) {
        var card = cardCreditRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        cardCreditRepository.delete(card);
    }

    public CardCreditResponse findCardCredit(UUID id) {
        return cardCreditRepository
                .findById(id)
                .map(CardCreditResponse::toEntityResponse)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<CardCreditResponse> allCards() {
       return cardCreditRepository
               .findAll()
               .stream()
               .map(CardCreditResponse::toEntityResponse)
               .collect(Collectors.toList());
    }

    //todo - metodo para gerar o qrcode

    }

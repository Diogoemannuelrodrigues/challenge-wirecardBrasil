package com.github.wirecard.config;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class GerarCartaoCredito {

    private static final int[][] PREFIXES = {
            {4},             // Visa
            {51, 52, 53, 54, 55}, // MasterCard
            {34, 37},        // American Express
            {6011, 65}       // Discover
    };

    public static String geraNumeroCvv() {
        Random random = new Random();
        int cvv = random.nextInt(1000);
        return String.format("%03d", cvv);
    }

    public static LocalDate generateRandomFutureDate() {
        LocalDate today = LocalDate.now();
        long daysInFourYears = ChronoUnit.DAYS.between(today, today.plusYears(4));

        Random random = new Random();
        long randomDays = random.nextInt((int) daysInFourYears + 1);

        return today.plusDays(randomDays);
    }

    public static String generateCreditCardNumber() {
        Random random = new Random();
        int[] selectedPrefix = PREFIXES[random.nextInt(PREFIXES.length)];
        StringBuilder cardNumberStr = new StringBuilder();

        // Escolhe um prefixo aleatório do conjunto selecionado
        int prefix = selectedPrefix[random.nextInt(selectedPrefix.length)];
        cardNumberStr.append(prefix);

        // Calcula o comprimento do número do cartão com base na bandeira (Amex tem 15 dígitos, outros têm 16)
        int cardNumberLength = (prefix == 34 || prefix == 37) ? 15 : 16;
        int remainingDigits = cardNumberLength - cardNumberStr.length() - 1;

        for (int i = 0; i < remainingDigits; i++) {
            cardNumberStr.append(random.nextInt(10));
        }

        int[] cardNumber = new int[cardNumberLength];
        for (int i = 0; i < cardNumberStr.length(); i++) {
            cardNumber[i] = Character.getNumericValue(cardNumberStr.charAt(i));
        }

        cardNumber[cardNumberLength - 1] = getCheckDigit(cardNumber);

        cardNumberStr.setLength(0); // Limpa o StringBuilder
        for (int digit : cardNumber) {
            cardNumberStr.append(digit);
        }

        return cardNumberStr.toString();
    }

    private static int getCheckDigit(int[] cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length - 2; i >= 0; i--) {
            int n = cardNumber[i];
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum * 9) % 10;
    }



}

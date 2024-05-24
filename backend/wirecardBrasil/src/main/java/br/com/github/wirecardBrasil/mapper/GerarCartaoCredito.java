package br.com.github.wirecardBrasil.mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class GerarCartaoCredito {

    public static String geraNumeroCvv(){
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

        int[] prefix = {4};

        int[] cardNumber = new int[16];
        cardNumber[0] = prefix[random.nextInt(prefix.length)];

        for (int i = 1; i < cardNumber.length - 1; i++) {
            cardNumber[i] = random.nextInt(10);
        }

        cardNumber[15] = getCheckDigit(cardNumber);

        StringBuilder cardNumberStr = new StringBuilder();
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

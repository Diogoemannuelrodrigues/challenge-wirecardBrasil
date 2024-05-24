package br.com.github.wirecardBrasil.config;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BarcodeGenerator {

    private static final String BANCO = "341"; // Código do banco
    private static final String MOEDA = "9"; // Código da moeda
    private static final String AGENCIA = "1234"; // Número da agência
    private static final String CONTA = "56789"; // Número da conta
    private static final String DV_AGENCIA_CONTA = "1"; // Dígito verificador da agência e conta

/*    public static void main(String[] args) {
        try {
            String codigoBarras = gerarCodigoBarras("2024-05-28", "100.00", "123456789", "400");
            codigoBarras = trimEspacos(codigoBarras);
            System.out.println(codigoBarras);
        } catch (Exception e) {
            System.out.println("Erro ao gerar o código de barras: " + e.getMessage());
        }
    }*/

    public static String gerarCodigoBarras(String dataVencimento, String valor, String nossoNumero, String valorTotal) {
        String fatorVencimento = calcularFatorVencimento(dataVencimento);

        StringBuilder codigoBarras = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

        codigoBarras.append(BANCO);
        codigoBarras.append(MOEDA);
        codigoBarras.append(calcularDVModulo10(BANCO + MOEDA));
        codigoBarras.append(fatorVencimento);
        codigoBarras.append(formatarValor(valor));
        codigoBarras.append(nossoNumero);
        codigoBarras.append(AGENCIA);
        codigoBarras.append(CONTA);
        codigoBarras.append(DV_AGENCIA_CONTA);
        codigoBarras.append(calcularDVModulo10(fatorVencimento + formatarValor(valor) + nossoNumero + AGENCIA + CONTA + DV_AGENCIA_CONTA));
        codigoBarras.append(formatarValor(valorTotal)); // Adiciona o valor total no final

        // Inserindo os espaços conforme a formatação
        codigoBarras.insert(5, " ");
        codigoBarras.insert(11, " ");
        codigoBarras.insert(17, " ");
        codigoBarras.insert(24, " ");
        codigoBarras.insert(30, " ");
        codigoBarras.insert(36, " ");
        codigoBarras.insert(38, " ");
        codigoBarras.insert(39, " ");
        codigoBarras.insert(40, " ");
        codigoBarras.insert(41, " ");

        return codigoBarras.toString();
    }

    public static String formatarValor(String valor) {
        // Remover pontos e vírgulas, se houver
        valor = valor.replace(".", "").replace(",", "");
        // Garantir que o valor tenha duas casas decimais
        if (!valor.contains(".")) {
            valor += "00";
        } else if (valor.indexOf(".") == valor.length() - 2) {
            valor += "0";
        }
        return valor;
    }

    public static String calcularFatorVencimento(String dataVencimento) {
        // Lógica para calcular o fator de vencimento
        return "1234";
    }

    public static String calcularDVModulo10(String numero) {
        int soma = 0;
        int peso = 2;
        int n = numero.length();

        for (int i = n - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            int mult = digito * peso;

            soma += mult >= 10 ? mult - 9 : mult;

            peso = peso == 2 ? 1 : 2;
        }

        int resto = soma % 10;
        int dv = resto == 0 ? 0 : 10 - resto;

        return String.valueOf(dv);
    }
    public static String trimEspacos(String codigoBarras) {
        StringBuilder sb = new StringBuilder();
        boolean ultimoEspaco = false;

        for (int i = 0; i < codigoBarras.length(); i++) {
            char c = codigoBarras.charAt(i);
            if (c == ' ') {
                if (!ultimoEspaco) {
                    sb.append(c);
                }
                ultimoEspaco = true;
            } else {
                sb.append(c);
                ultimoEspaco = false;
            }
        }

        return sb.toString();
    }
}
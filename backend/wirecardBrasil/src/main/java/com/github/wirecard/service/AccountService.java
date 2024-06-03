package com.github.wirecard.service;

import com.github.wirecard.entidade.Account;
import com.github.wirecard.repository.AccountRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
//    public static void main(String[] args) {
//        String chavePIX = "(11) 11111-1111";
//        String nome = "1111";
//        String valor = "111.00";
//        String codigoTransferencia = "11";
//        String tipoChave = "Telefone";
//
//        String payloadPIX = generatePIXPayload(chavePIX, nome, tipoChave, valor, codigoTransferencia);
//        String filePath = "teste.png";
//        int width = 300;
//        int height = 300;
//        String fileType = "png";
//
//        try {
//            generateQRCodeImage(payloadPIX, width, height, filePath, fileType);
//            System.out.println("QR Code PIX gerado com sucesso em: " + filePath);
//            //todo - gerar mesnagem que qrcode foi gerado
//        } catch (WriterException | IOException e) {
//            System.out.println("Erro ao gerar QR Code: " + e.getMessage());
//        }
//    }
    public static String generatePIXPayload(String chavePIX, String nome, String tipoChave, String valor, String codigoTransferencia) {
        // Estrutura básica do Payload PIX com informações fornecidas
        return "000201" + // Payload format indicator
                "26360014BR.GOV.BCB.PIX" + // Merchant Account Information template
                "01" + String.format("%02d", chavePIX.length()) + chavePIX + // Chave PIX
                "52040000" + // Merchant Category Code (MCC)
                "5303986" + // Transaction currency (986 para BRL)
                "54" + String.format("%02d", valor.length() + 1) + valor + // Valor
                "5802BR" + // País
                "59" + String.format("%02d", nome.length()) + nome + // Nome do recebedor
                "6007" + tipoChave + // Tipo de chave
                "62070503***" + // Código da Transferência
                "6304"; // CRC16 (a ser calculado)
    }

    public static void generateQRCodeImage(String text, int width, int height, String filePath, String fileType)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, fileType, new File(filePath));
    }

    public void fecharFatura(Account account) {
        double total = account.calcularTotal();
        System.out.println("Fatura fechada em " + LocalDate.now() + ". Total devido: R$ " + total);
        //todo - send email for this client with the account closed
        //todo - verificar se pode limpar pois tem parcelas
        account.getTransacoes().clear();

    }

}

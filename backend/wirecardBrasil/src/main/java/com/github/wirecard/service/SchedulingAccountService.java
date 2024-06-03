package com.github.wirecard.service;

import com.github.wirecard.entidade.Account;
import com.github.wirecard.entidade.Transaction;
import com.github.wirecard.service.clients.ValidateCardCreditClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor

public class SchedulingAccountService {
    private final AccountService accountService;
    private final ValidateCardCreditClient validateCardCreditClient;
    private final CardCreditService cardCreditService;


    public void transaction(Transaction transaction){
        validateTransaction(transaction);

    }

    public TimerTask closedAccount(Account account) {
        return new TimerTask() {
            @Override
            public void run() {
                accountService.fecharFatura(account);
                //todo - gerar mensagem de fechamento do cartaao
            }
        };
    }

    public void scheduleAccountClosure(Account account){
        TimerTask task = closedAccount(account);

        Timer timer = new Timer();
        Calendar cal = Calendar.getInstance();

        // Configura para o último dia do mês às 23:59
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date dataFechamento = cal.getTime();

        // Agenda a tarefa para ser executada no final do mês
        timer.schedule(task, dataFechamento, 1000L * 60 * 60 * 24 * 30); // Reexecuta a cada 30 dias
        //todo - gerar mensagem que a fatura fechou
        // Para teste, imprime a fatura atual
//        System.out.println("Fatura Atual: " + cartao.calcularTotal());
        log.info("Fatura Atual: ", account.calcularTotal());
    }

    public Transaction validateTransaction(Transaction transaction){
        var trans = validateCardCreditClient.validateTransaction(transaction);

        if("true".equalsIgnoreCase(trans))
            Transaction.builder()
                    .transactionId(UUID.randomUUID())
                    .value(transaction.getValue())
                    .date(LocalDate.now())
                    .description(transaction.getDescription())
                    .typePayment(transaction.getTypePayment())
                    .build();

        //todo - veridicar o valor da transicao e ver se tem saldo no cartao de credito
        return null;
    }
}


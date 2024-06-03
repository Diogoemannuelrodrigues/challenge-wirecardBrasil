package com.github.wirecard.service.clients;

import com.github.wirecard.entidade.CardCredit;
import com.github.wirecard.entidade.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cardCredit", url = "https://run.mocky.io/v3")
public interface ValidateCardCreditClient {

    //todo - criar records para alterar o objeto

    @PostMapping("/4f4b62af-6a1b-4af5-8bf4-48eeca6c60c1")
    String validate(@RequestBody CardCredit cardCredit);

    @PostMapping("/5661a8f1-0755-419c-96f2-ea5f59c5d499")
    String validateTransactionFail(@RequestBody Transaction transaction);

    @PostMapping("/5661a8f1-0755-419c-96f2-ea5f59c5d499")
    String validateTransaction(@RequestBody Transaction transaction);


}

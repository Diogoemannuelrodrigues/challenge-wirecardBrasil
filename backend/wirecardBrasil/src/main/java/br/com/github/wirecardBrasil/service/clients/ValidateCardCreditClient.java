package br.com.github.wirecardBrasil.service.clients;

import br.com.github.wirecardBrasil.entidade.CardCredit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cardCredit", url = "https://run.mocky.io/v3/4f4b62af-6a1b-4af5-8bf4-48eeca6c60c1")
public interface ValidateCardCreditClient {

    @PostMapping
    String validate(@RequestBody CardCredit cardCredit);
}

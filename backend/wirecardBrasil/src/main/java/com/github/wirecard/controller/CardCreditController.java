package com.github.wirecard.controller;

import com.github.wirecard.entidade.record.CardCreditRequest;
import com.github.wirecard.entidade.record.CardCreditResponse;
import com.github.wirecard.service.CardCreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/card-credit")
@RequiredArgsConstructor
public class CardCreditController {

    private final CardCreditService cardCreditService;

    @PostMapping("/create")
    public ResponseEntity<CardCreditResponse> createCardCredit(@RequestBody CardCreditRequest creditRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(cardCreditService.genereteCardCerdit(creditRequest));
    }

    @DeleteMapping("/{id}")
    public void createCardCredit(@PathVariable UUID id){
        cardCreditService.destroyCardCredit(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardCreditResponse> statusCardCredit(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(cardCreditService.findCardCredit(id));
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardCreditResponse>> cards() {
        return ResponseEntity.status(HttpStatus.OK).body(cardCreditService.allCards());
    }

}

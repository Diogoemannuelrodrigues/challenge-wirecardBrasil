package com.github.wirecard.controller;

import com.github.wirecard.entidade.record.ClientRequest;
import com.github.wirecard.entidade.record.ClientResponse;
import com.github.wirecard.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest clientRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(clientRequest));
    }
}

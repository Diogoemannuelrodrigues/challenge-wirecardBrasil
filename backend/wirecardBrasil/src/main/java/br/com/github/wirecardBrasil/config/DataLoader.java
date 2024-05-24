package br.com.github.wirecardBrasil.config;

import br.com.github.wirecardBrasil.entidade.Buyer;
import br.com.github.wirecardBrasil.entidade.Client;
import br.com.github.wirecardBrasil.repository.BuyerRepository;
import br.com.github.wirecardBrasil.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final BuyerRepository buyerRepository;

    public DataLoader(ClientRepository clientRepository, BuyerRepository buyerRepository) {
        this.clientRepository = clientRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var buyer = Buyer.builder()
                .cpf("604.500.930-42")
                .email("Teste@gmail.com")
                .id(1L)
                .nome("Buyer teste")
                .build();

        Client client = Client.builder()
                .id(1L)
                .cpf("711.335.890-06")
                .nome("Diogo")
                .build();

        buyerRepository.save(buyer);
        clientRepository.save(client);

    }
}

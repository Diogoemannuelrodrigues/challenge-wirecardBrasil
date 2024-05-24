package br.com.github.wirecardBrasil.repository;

import br.com.github.wirecardBrasil.entidade.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}

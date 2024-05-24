package br.com.github.wirecardBrasil.repository;

import br.com.github.wirecardBrasil.entidade.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Long> {
}

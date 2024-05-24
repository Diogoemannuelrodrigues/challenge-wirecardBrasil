package br.com.github.wirecardBrasil.repository;

import br.com.github.wirecardBrasil.entidade.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}

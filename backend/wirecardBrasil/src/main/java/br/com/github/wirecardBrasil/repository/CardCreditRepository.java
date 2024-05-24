package br.com.github.wirecardBrasil.repository;

import br.com.github.wirecardBrasil.entidade.CardCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardCreditRepository extends JpaRepository<CardCredit, Integer> {
}

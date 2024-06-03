package com.github.wirecard.repository;

import com.github.wirecard.entidade.CardCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardCreditRepository extends JpaRepository<CardCredit, UUID> {
}

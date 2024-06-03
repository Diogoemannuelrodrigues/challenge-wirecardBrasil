package com.github.wirecard.repository;

import com.github.wirecard.entidade.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}

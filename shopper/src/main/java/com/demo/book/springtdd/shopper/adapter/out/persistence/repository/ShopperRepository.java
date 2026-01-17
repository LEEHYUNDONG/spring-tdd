package com.demo.book.springtdd.shopper.adapter.out.persistence.repository;

import com.demo.book.springtdd.shopper.domain.Shopper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopperRepository extends JpaRepository<Shopper, Long> {
    Optional<Shopper> findByEmail(String email);

    Optional<Shopper> findByUsername(String username);

    Optional<Shopper> findById(UUID id);
}

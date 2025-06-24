package com.demo.book.springtdd.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopperRepository extends JpaRepository<Shopper, Long> {
    Optional<Shopper> findByEmail(String email);

    Optional<Shopper> findByUsername(String username);

    Optional<Shopper> findById(UUID id);

    // This class can be extended to add custom methods for querying the database
    // For example, you might want to find a shopper by email or username

    // Example method:
    // Optional<Shopper> findByEmail(String email);
}

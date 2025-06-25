package com.demo.book.springtdd.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    // Example method:
    // Optional<Product> findByName(String name);
    Optional<Product> findById(UUID id);
}

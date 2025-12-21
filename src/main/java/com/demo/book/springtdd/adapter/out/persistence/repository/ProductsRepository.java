package com.demo.book.springtdd.adapter.out.persistence.repository;

import com.demo.book.springtdd.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    // Example method:
    Optional<Product> findById(UUID id);

    List<Product> findBySellerId(UUID sellerId);
}

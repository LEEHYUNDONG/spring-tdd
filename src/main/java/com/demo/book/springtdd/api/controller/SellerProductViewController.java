package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
public record SellerProductViewController(
        ProductsRepository productsRepository
) {

    @GetMapping("/seller/products/{id}")
    public ResponseEntity<?> viewProducts(@PathVariable UUID id, Principal user) {
        UUID sellerId = UUID.fromString(user.getName());
        return productsRepository.findById(id)
                .filter(product -> product.getSellerId().equals(sellerId))
                .map(product -> new SellerProductView(
                        product.getId(),
                        product.getName(),
                        product.getImageUri(),
                        product.getDescription(),
                        product.getPriceAmount(),
                        product.getStockQuantity(),
                        null
                ))
                .map(product -> ResponseEntity.ok(product))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}

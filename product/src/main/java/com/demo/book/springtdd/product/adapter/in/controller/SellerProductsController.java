package com.demo.book.springtdd.product.adapter.in.controller;

import com.demo.book.springtdd.product.adapter.in.dto.command.RegisterProductCommand;
import com.demo.book.springtdd.product.domain.model.command.RegisterProductCommandExecutor;
import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductsController(ProductsRepository productsRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> addProduct(@RequestBody RegisterProductCommand command, Principal principal) {
        UUID id = UUID.randomUUID();
        var executor = new RegisterProductCommandExecutor(productsRepository::save);
        executor.execute(id, UUID.fromString(principal.getName()), command);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }
}

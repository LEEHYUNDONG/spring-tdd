package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.commandmodel.RegisterProductCommandExecutor;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.domain.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductsController(SellerRepository sellerRepository, ProductsRepository productsRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> addProduct(@RequestBody RegisterProductCommand command, Principal principal) {
        UUID id = UUID.randomUUID();
        var executor = new RegisterProductCommandExecutor(productsRepository::save);
        executor.execute(id, UUID.fromString(principal.getName()), command);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }
}

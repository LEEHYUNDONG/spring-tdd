package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.commandmodel.InvalidCommandException;
import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@RestController
public record SellerProductsController(SellerRepository sellerRepository, ProductsRepository productsRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> addProduct(@RequestBody RegisterProductCommand command, Principal principal) {
        UUID sellerId = UUID.fromString(principal.getName());
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        if(isValidUri(command.imageUri()) == false) {
            throw new InvalidCommandException();
        }
        UUID id = UUID.randomUUID();
        var product = new Product();
        product.setId(id);
        product.setName(command.name());
        product.setSellerId(sellerId);
        product.setImageUri(command.imageUri());
        product.setDescription(command.description());
        product.setPriceAmount(command.priceAmount());
        product.setStockQuantity(command.stockQunatity());
        product.setRegisteredAt(LocalDateTime.now(UTC));
        productsRepository.save(product);

        URI location = URI.create("/seller/products/" + product.getId());
        return ResponseEntity.created(location).build();
    }

    private static boolean isValidUri(String imageUri) {
        try{
            URI uri = URI.create(imageUri);
            return uri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.RegisterProductCommand;
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
import java.util.Optional;
import java.util.UUID;

@RestController
public record SellerProductController(SellerRepository sellerRepository, ProductsRepository productsRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> addProduct(Principal user, @RequestBody RegisterProductCommand command, Principal principal) {
        UUID id = UUID.fromString(user.getName());
        Optional<Seller> seller = sellerRepository.findById(id);

        if(seller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(isValidUri(command.imageUri()) == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Product product = new Product();
        product.setId(UUID.fromString(user.getName()));
        product.setProductName(command.productName());
        product.setSellerId(UUID.fromString(principal.getName()));
        product.setImageUri(command.imageUri());
        product.setDescription(command.description());
        product.setPriceAmount(command.priceAmount());
        product.setStockQuantity(command.stockQunatity());
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

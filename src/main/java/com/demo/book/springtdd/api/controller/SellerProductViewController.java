package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
public record SellerProductViewController(
        SellerRepository sellerRepository,
        ProductsRepository productsRepository
) {

    @GetMapping("/seller/products/{productId}")
    public ResponseEntity<?> viewProducts(@PathVariable String productId, Principal user) {
        UUID id = UUID.fromString(user.getName());
        Optional<Seller> seller = sellerRepository.findById(id);
        System.out.println("seller = " + seller);

        if(seller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(!productId.equals(id.toString())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().build();
    }
}

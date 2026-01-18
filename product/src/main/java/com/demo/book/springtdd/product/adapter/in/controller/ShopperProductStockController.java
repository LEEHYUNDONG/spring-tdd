package com.demo.book.springtdd.product.adapter.in.controller;

import com.demo.book.springtdd.exception.InvalidCommandException;
import com.demo.book.springtdd.product.adapter.in.dto.command.DecreaseStockCommand;
import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ShopperProductStockController {

    private final ProductsRepository productsRepository;

    public ShopperProductStockController(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Transactional
    @PostMapping("/shopper/products/{id}/decrease-stock")
    ResponseEntity<?> decreaseStock(
            @PathVariable("id") UUID productId,
            @RequestBody DecreaseStockCommand command
    ) {
        if (command.quantity() <= 0) {
            throw new InvalidCommandException();
        }

        var product = productsRepository.findById(productId)
                .orElseThrow(InvalidCommandException::new);

        int remaining = product.getStockQuantity() - command.quantity();
        if (remaining < 0) {
            throw new InvalidCommandException();
        }

        product.setStockQuantity(remaining);
        productsRepository.save(product);
        return ResponseEntity.noContent().build();
    }
}

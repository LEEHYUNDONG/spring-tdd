package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.view.ArrayCarrier;
import com.demo.book.springtdd.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Comparator;
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
                .map(SellerProductViewController::convertToView)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/products")
    ResponseEntity<?> getProducts(Principal user) {
        UUID sellerId = UUID.fromString(user.getName()); // Replace with actual seller ID retrieval logic

        SellerProductView[] items = productsRepository
                .findBySellerId(sellerId)
                .stream()
                .map(SellerProductViewController::convertToView)
                .sorted(Comparator.comparing(SellerProductView::registeredAt).reversed())
                .toArray(SellerProductView[]::new);

        return ResponseEntity.ok(new ArrayCarrier<SellerProductView>(items));
    }

    private static SellerProductView convertToView(Product product) {
        return new SellerProductView(
                product.getId(),
                product.getName(),
                product.getImageUri(),
                product.getDescription(),
                product.getPriceAmount(),
                product.getStockQuantity(),
                product.getRegisteredAt()
        );
    }

}

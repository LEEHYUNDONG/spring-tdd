package com.demo.book.springtdd.product.adapter.in.controller;

import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import com.demo.book.springtdd.product.adapter.in.dto.query.FindSellerProduct;
import com.demo.book.springtdd.product.adapter.in.dto.query.GetSellerProducts;
import com.demo.book.springtdd.product.domain.model.query.FindSellerProoductQueryProcessor;
import com.demo.book.springtdd.product.domain.model.query.GetSellerProductsQueryProcessor;
import com.demo.book.springtdd.product.adapter.in.dto.view.ArrayCarrier;
import com.demo.book.springtdd.product.adapter.in.dto.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductViewController(
        ProductsRepository productsRepository
) {

    @GetMapping("/seller/products/{id}")
    public ResponseEntity<?> viewProducts(@PathVariable UUID id, Principal user) {
        var processor = new FindSellerProoductQueryProcessor(productsRepository::findById);
        var query = new FindSellerProduct(UUID.fromString(user.getName()), id);

        return ResponseEntity.of(processor.process(query));
    }

    @GetMapping("/seller/products")
    ArrayCarrier<SellerProductView> getProducts(Principal user) {
        var processor = new GetSellerProductsQueryProcessor(productsRepository::findBySellerId);
        var query = new GetSellerProducts(UUID.fromString(user.getName()));
        return processor.process(query);
    }
}

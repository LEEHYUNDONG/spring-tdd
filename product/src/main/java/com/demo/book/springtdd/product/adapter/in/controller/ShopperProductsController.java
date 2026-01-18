package com.demo.book.springtdd.product.adapter.in.controller;

import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import com.demo.book.springtdd.product.adapter.in.dto.query.GetProductPage;
import com.demo.book.springtdd.product.domain.model.query.GetProductDetailQueryProcessor;
import com.demo.book.springtdd.product.domain.model.query.GetProductPageQueryProcessor;
import com.demo.book.springtdd.product.adapter.in.dto.result.PageCarrier;
import com.demo.book.springtdd.product.adapter.in.dto.view.ProductView;

import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public record ShopperProductsController(
    ProductsRepository productsRepository,
    EntityManager em
) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(
            @RequestParam(required = false) String continuationToken
    ) {
        var processor = new GetProductPageQueryProcessor(em);
        var query = new GetProductPage(continuationToken);
        return processor.process(query);
    }

    @GetMapping("/shopper/products/{id}")
    ProductView getProduct(@PathVariable("id") UUID id) {
        var processor = new GetProductDetailQueryProcessor(em);
        return processor.process(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



}

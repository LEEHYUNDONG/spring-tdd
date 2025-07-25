package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.query.GetProductPage;
import com.demo.book.springtdd.querymodel.GetProductPageQueryProcessor;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.view.ProductView;

import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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



}

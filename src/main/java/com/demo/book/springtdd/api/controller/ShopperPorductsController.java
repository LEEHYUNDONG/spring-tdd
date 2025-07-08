package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.view.ProductView;
import org.hibernate.query.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@RestController
public record ShopperPorductsController(
    ProductsRepository productsRepository
) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts() {
        ProductView[] items = productsRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getDataKey).reversed())
                .map(product -> new ProductView(
                        product.getId(),
                        null,
                        product.getName(),
                        product.getImageUri(),
                        product.getDescription(),
                        product.getPriceAmount(),
                        product.getStockQuantity()
                )).toArray(ProductView[]::new);
        return new PageCarrier<>(items, null);
    }

}

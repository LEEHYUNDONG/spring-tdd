package com.demo.book.springtdd.api;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.view.ProductView;
import com.demo.book.springtdd.view.SellerView;

public record ProductSellerTuple(Product product, Seller seller) {

    public static ProductView toView(ProductSellerTuple tuple) {
        return new ProductView(
                tuple.product().getId(),
                new SellerView(
                        tuple.seller().getId(),
                        tuple.seller().getUsername(),
                        null
                ),
                tuple.product().getName(),
                tuple.product().getImageUri(),
                tuple.product().getDescription(),
                tuple.product().getPriceAmount(),
                tuple.product().getStockQuantity()
        );
    }
}

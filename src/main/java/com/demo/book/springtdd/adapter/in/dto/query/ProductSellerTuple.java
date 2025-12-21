package com.demo.book.springtdd.adapter.in.dto.query;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.adapter.in.dto.view.ProductView;
import com.demo.book.springtdd.adapter.in.dto.view.SellerView;

public record ProductSellerTuple(Product product, Seller seller) {

    public static ProductView toView(ProductSellerTuple tuple) {
        return new ProductView(
                tuple.product().getId(),
                new SellerView(
                        tuple.seller().getId(),
                        tuple.seller().getUsername(),
                        tuple.seller().getContactEmail()
                ),
                tuple.product().getName(),
                tuple.product().getImageUri(),
                tuple.product().getDescription(),
                tuple.product().getPriceAmount(),
                tuple.product().getStockQuantity()
        );
    }
}

package com.demo.book.springtdd.product.domain.model.query;

import com.demo.book.springtdd.product.domain.Product;
import com.demo.book.springtdd.product.adapter.in.dto.view.ProductView;
import com.demo.book.springtdd.product.adapter.in.dto.view.SellerView;
import com.demo.book.springtdd.seller.domain.Seller;

public record ProductSellerTuple(Product product, Seller seller) {

    public ProductView toView() {
        return new ProductView(
                product().getId(),
                new SellerView(seller().getId(), seller().getUsername(), seller.getContactEmail()),
                product().getName(),
                product().getImageUri(),
                product().getDescription(),
                product().getPriceAmount(),
                product().getStockQuantity()
        );
    }
}

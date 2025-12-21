package com.demo.book.springtdd.domain.model.query;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.adapter.in.dto.view.ProductView;
import com.demo.book.springtdd.adapter.in.dto.view.SellerView;

record ProductSellerTuple(Product product, Seller seller) {

    ProductView toView() {
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

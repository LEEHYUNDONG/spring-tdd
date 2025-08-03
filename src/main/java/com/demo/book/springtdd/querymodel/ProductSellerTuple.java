package com.demo.book.springtdd.querymodel;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.view.ProductView;
import com.demo.book.springtdd.view.SellerView;

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

package com.demo.book.springtdd.product.domain.mapper;

import com.demo.book.springtdd.product.application.ProductView;
import com.demo.book.springtdd.product.application.SellerView;
import com.demo.book.springtdd.product.domain.Product;
import com.demo.book.springtdd.seller.domain.Seller;

public class ProductMapper {

    public static ProductView toProto(Product product, Seller seller) {
        return ProductView.newBuilder()
                .setId(product.getId().toString())
                .setSeller(SellerView.newBuilder()
                        .setId(seller.getId().toString())
                        .setUsername(seller.getUsername())
                        .setContactEmail(seller.getContactEmail() != null ? seller.getContactEmail() : "")
                        .build())
                .setName(product.getName())
                .setImageUri(product.getImageUri() != null ? product.getImageUri() : "")
                .setDescription(product.getDescription() != null ? product.getDescription() : "")
                .setPriceAmount(product.getPriceAmount().longValue())
                .setStockQuantity(product.getStockQuantity())
                .build();
    }
}

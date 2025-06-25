package com.demo.book.springtdd.view;

import java.math.BigDecimal;

public record SellerProductView(
        String id,
        String productName,
        String imageUri,
        String description,
        BigDecimal priceAmount,
        int stockQuantity,
        String registeredTimeUtc
) {
}

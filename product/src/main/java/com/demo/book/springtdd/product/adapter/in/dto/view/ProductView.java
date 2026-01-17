package com.demo.book.springtdd.product.adapter.in.dto.view;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductView(
        UUID id,
        SellerView seller,
        String name,
        String imageUri,
        String description,
        BigDecimal priceAmount,
        int stockQuantity
) {
}

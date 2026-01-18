package com.demo.book.springtdd.order.adapter.out.grpc.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
        UUID id,
        SellerDto seller,
        String name,
        String imageUri,
        String description,
        BigDecimal priceAmount,
        int stockQuantity
) {
}

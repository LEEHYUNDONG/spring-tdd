package com.demo.book.springtdd.order.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Order(
        UUID id,
        UUID productId,
        UUID sellerId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount
) {
}

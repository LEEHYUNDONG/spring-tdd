package com.demo.book.springtdd.order.adapter.in.dto.view;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderView(
        UUID id,
        UUID productId,
        UUID sellerId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount
) {
}

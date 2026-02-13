package com.demo.book.springtdd.order.adapter.in.dto;

import java.util.UUID;

public record CreateOrderRequest(
        UUID productId,
        int quantity
) {
}

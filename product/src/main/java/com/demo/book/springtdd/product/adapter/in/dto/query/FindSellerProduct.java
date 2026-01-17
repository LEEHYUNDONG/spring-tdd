package com.demo.book.springtdd.product.adapter.in.dto.query;

import java.util.UUID;

public record FindSellerProduct(UUID sellerId, UUID productId) {
}

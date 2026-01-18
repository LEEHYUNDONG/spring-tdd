package com.demo.book.springtdd.order.adapter.out.grpc.dto;

import java.util.UUID;

public record SellerDto(
        UUID id,
        String username,
        String contactEmail
) {
}

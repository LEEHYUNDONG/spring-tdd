package com.demo.book.springtdd.seller.application.port.in.result;

import java.util.UUID;

public record SellerInfo(
        UUID id,
        String email,
        String username,
        String contactEmail
) {
}

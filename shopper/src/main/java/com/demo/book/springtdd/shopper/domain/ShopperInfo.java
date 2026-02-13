package com.demo.book.springtdd.shopper.domain;

import java.util.UUID;

public record ShopperInfo(
        UUID id,
        String email,
        String username
) {
}

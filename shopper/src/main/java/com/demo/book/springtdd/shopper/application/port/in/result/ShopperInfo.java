package com.demo.book.springtdd.shopper.application.port.in.result;

import java.util.UUID;

public record ShopperInfo(
        UUID id,
        String email,
        String username
) {
}

package com.demo.book.springtdd.infrastructure;

import javax.crypto.SecretKey;

public record JwtKeyHolder(
        SecretKey secretKey,
        int expirationHours,
        String issuer
) {
}

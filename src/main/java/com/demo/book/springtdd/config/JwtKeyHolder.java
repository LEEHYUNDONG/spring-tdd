package com.demo.book.springtdd.config;

import javax.crypto.SecretKey;

public record JwtKeyHolder(
        SecretKey secretKey,
        int expirationHours,
        String issuer
) {
}

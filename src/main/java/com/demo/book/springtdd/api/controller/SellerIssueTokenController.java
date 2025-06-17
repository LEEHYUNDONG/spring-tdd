package com.demo.book.springtdd.api.controller;


import com.demo.book.springtdd.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;

@RestController
public record SellerIssueTokenController(
        @Value("${security.jwt.secret}") String jwtSecret
) {

    @PostMapping("/seller/issueToken")
    AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier(Jwts
                .builder()
                .signWith(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256")).compact());
    }
}

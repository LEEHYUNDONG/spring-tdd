package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.config.JwtKeyHolder;
import com.demo.book.springtdd.domain.ShopperRepository;
import com.demo.book.springtdd.query.IssueShopperToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperIssueTokenController(JwtKeyHolder jwtKeyHolder, PasswordEncoder passwordEncoder, ShopperRepository shopperRepository) {

    @PostMapping("/shopper/issueToken")
    public ResponseEntity<AccessTokenCarrier> composeToken(@RequestBody IssueShopperToken qeury) {
        return shopperRepository.findByEmail(qeury.email())
                .filter(shopper -> passwordEncoder.matches(
                        qeury.password(),
                        shopper.getHashedPassword()))
                .map(shopper -> composeToken())
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
                .builder()
                .signWith(jwtKeyHolder.secretKey()).compact();
    }

}

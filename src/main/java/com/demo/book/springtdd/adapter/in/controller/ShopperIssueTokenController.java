package com.demo.book.springtdd.adapter.in.controller;

import com.demo.book.springtdd.infrastructure.JwtKeyHolder;
import com.demo.book.springtdd.domain.Shopper;
import com.demo.book.springtdd.adapter.out.persistence.repository.ShopperRepository;
import com.demo.book.springtdd.adapter.in.dto.query.IssueShopperToken;
import com.demo.book.springtdd.adapter.in.dto.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public record ShopperIssueTokenController(JwtKeyHolder jwtKeyHolder, PasswordEncoder passwordEncoder, ShopperRepository shopperRepository) {

    @PostMapping("/shopper/issueToken")
    public ResponseEntity<AccessTokenCarrier> composeToken(@RequestBody IssueShopperToken qeury) {
        return shopperRepository.findByEmail(qeury.email())
                .filter(shopper -> passwordEncoder.matches(
                        qeury.password(),
                        shopper.getHashedPassword()))
                .map(this::composeToken)
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken(Shopper shopper) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtKeyHolder.expirationHours(), ChronoUnit.HOURS);
        
        return Jwts
                .builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString()) // jti (JWT ID)
                .setIssuer(jwtKeyHolder.issuer()) // iss (Issuer)
                .setSubject(shopper.getId().toString()) // sub (Subject)
                .setIssuedAt(Date.from(now)) // iat (Issued At)
                .setExpiration(Date.from(expiration)) // exp (Expiration Time)
                .claim("scp", "shopper") // scope
                .claim("email", shopper.getEmail()) // 사용자 이메일 추가
                .signWith(jwtKeyHolder.secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}

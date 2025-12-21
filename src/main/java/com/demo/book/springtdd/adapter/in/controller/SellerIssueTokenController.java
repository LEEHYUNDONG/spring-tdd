package com.demo.book.springtdd.adapter.in.controller;


import com.demo.book.springtdd.infrastructure.JwtKeyHolder;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.adapter.in.dto.query.IssueSellerToken;
import com.demo.book.springtdd.adapter.in.dto.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RestController
public record SellerIssueTokenController(
        JwtKeyHolder jwtKeyHolder,
        PasswordEncoder passwordEncoder,
        SellerRepository sellerRepository
) {

    @PostMapping("/seller/issueToken")
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerToken issueSellerToken) {
        return sellerRepository.findByEmail(issueSellerToken.email())
                .filter(seller -> passwordEncoder.matches(
                        issueSellerToken.password(),
                        seller.getHashedPassword()))
                .map(this::composeToken)
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken(Seller seller) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtKeyHolder.expirationHours(), ChronoUnit.HOURS);
        
        return Jwts
                .builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString()) // jti (JWT ID)
                .setIssuer(jwtKeyHolder.issuer()) // iss (Issuer)
                .setSubject(seller.getId().toString()) // sub (Subject)
                .setIssuedAt(Date.from(now)) // iat (Issued At)
                .setExpiration(Date.from(expiration)) // exp (Expiration Time)
                .claim("scp", "seller") // scope
                .claim("email", seller.getEmail()) // 사용자 이메일 추가
                .signWith(jwtKeyHolder.secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

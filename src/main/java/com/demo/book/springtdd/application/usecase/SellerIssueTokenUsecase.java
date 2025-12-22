package com.demo.book.springtdd.application.usecase;

import com.demo.book.springtdd.adapter.in.dto.query.IssueSellerToken;
import com.demo.book.springtdd.application.port.in.ForIssuingSellerToken;
import com.demo.book.springtdd.application.port.out.ReadSellerPort;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.infrastructure.JwtKeyHolder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SellerIssueTokenUsecase implements ForIssuingSellerToken {

    private final ReadSellerPort readSellerPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtKeyHolder jwtKeyHolder;

    @Override
    public String issueToken(IssueSellerToken query) {
        Seller seller = readSellerPort.findByEmail(query.email());

        if (!passwordEncoder.matches(query.password(), seller.getHashedPassword())) {
            throw new EntityNotFoundException();
        }

        return composeToken(seller);
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
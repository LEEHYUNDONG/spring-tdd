package com.demo.book.springtdd.shopper.application.usecase;

import com.demo.book.springtdd.infrastructure.JwtKeyHolder;
import com.demo.book.springtdd.shopper.application.port.in.query.IssueShopperTokenQuery;
import com.demo.book.springtdd.shopper.application.port.in.ForIssuingShopperToken;
import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
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
public class ShopperIssueTokenUsecase implements ForIssuingShopperToken {

    private final ReadShopperPort readShopperPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtKeyHolder jwtKeyHolder;

    @Override
    public String issueToken(IssueShopperTokenQuery query) {
        Shopper shopper = readShopperPort.findByEmail(query.email());

        if (!passwordEncoder.matches(query.password(), shopper.getHashedPassword())) {
            throw new EntityNotFoundException();
        }

        return composeToken(shopper);
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

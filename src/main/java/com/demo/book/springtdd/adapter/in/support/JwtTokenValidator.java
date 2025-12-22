package com.demo.book.springtdd.adapter.in.support;

import com.demo.book.springtdd.infrastructure.JwtKeyHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenValidator {
    
    private final JwtKeyHolder jwtKeyHolder;
    
    public JwtTokenValidator(JwtKeyHolder jwtKeyHolder) {
        this.jwtKeyHolder = jwtKeyHolder;
    }
    
    public Optional<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyHolder.secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // 토큰 만료 시간 검증
            if (claims.getExpiration().before(new Date())) {
                return Optional.empty();
            }
            
            // 발행자 검증
            if (!jwtKeyHolder.issuer().equals(claims.getIssuer())) {
                return Optional.empty();
            }
            
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public boolean isTokenExpired(String token) {
        return validateToken(token)
                .map(claims -> claims.getExpiration().before(new Date()))
                .orElse(true);
    }
    
    public Optional<String> getSubject(String token) {
        return validateToken(token)
                .map(Claims::getSubject);
    }
    
    public Optional<String> getScope(String token) {
        return validateToken(token)
                .map(claims -> claims.get("scp", String.class));
    }
}

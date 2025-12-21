package com.demo.book.springtdd.integration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.ThrowingConsumer;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtAssertions {

    public static ThrowingConsumer<String> conformsToJwtFormat() {
        return s -> {
            String[] parts = s.split("\\.");
            assertThat(parts).hasSize(3);
            assertThat(parts[0]).matches(JwtAssertions::isBase64UrlEncodedJson);
            assertThat(parts[1]).matches(JwtAssertions::isBase64UrlEncodedJson);
            assertThat(parts[2]).matches(JwtAssertions::isBase64UrlEncoded);
        };
    }

    public static ThrowingConsumer<String> hasValidExpiration() {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getExpiration()).isAfter(new Date());
        };
    }

    public static ThrowingConsumer<String> hasValidIssuer(String expectedIssuer) {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getIssuer()).isEqualTo(expectedIssuer);
        };
    }

    public static ThrowingConsumer<String> hasValidIssuedAt() {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getIssuedAt()).isBeforeOrEqualTo(new Date());
        };
    }

    public static ThrowingConsumer<String> hasValidJti() {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getId()).isNotNull();
            assertThat(claims.getId()).isNotEmpty();
        };
    }

    public static ThrowingConsumer<String> hasValidScope(String expectedScope) {
        return token -> {
            Claims claims = parseClaims(token);
            String scope = claims.get("scp", String.class);
            assertThat(scope).isEqualTo(expectedScope);
        };
    }

    public static ThrowingConsumer<String> hasValidSubject() {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getSubject()).isNotNull();
            assertThat(claims.getSubject()).isNotEmpty();
        };
    }

    public static ThrowingConsumer<String> hasValidEmail() {
        return token -> {
            Claims claims = parseClaims(token);
            String email = claims.get("email", String.class);
            assertThat(email).isNotNull();
            assertThat(email).contains("@");
        };
    }

    public static ThrowingConsumer<String> hasValidAlgorithm() {
        return token -> {
            String[] parts = token.split("\\.");
            String header = new String(Base64.getUrlDecoder().decode(parts[0]));
            assertThat(header).contains("\"alg\":\"HS256\"");
            assertThat(header).contains("\"typ\":\"JWT\"");
        };
    }

    public static ThrowingConsumer<String> isNotExpired() {
        return token -> {
            Claims claims = parseClaims(token);
            assertThat(claims.getExpiration()).isAfter(new Date());
        };
    }

    public static ThrowingConsumer<String> expiresInHours(int hours) {
        return token -> {
            Claims claims = parseClaims(token);
            Date expiration = claims.getExpiration();
            Date issuedAt = claims.getIssuedAt();
            
            long diffInMillis = expiration.getTime() - issuedAt.getTime();
            long diffInHours = diffInMillis / (1000 * 60 * 60);
            
            assertThat(diffInHours).isEqualTo(hours);
        };
    }

    private static Claims parseClaims(String token) {
        // 테스트용 시크릿 키 (실제 환경과 동일)
        String secret = "c2f3e8b4-9d4b-4a6e-8f3e-2d7c9a1b5e6f";
        return Jwts.parserBuilder()
                .setSigningKey(new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isBase64UrlEncodedJson(String s1) {
        try {
            new ObjectMapper().readTree(Base64.getUrlDecoder().decode(s1));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBase64UrlEncoded(String s) {
        try{
            Base64.getUrlDecoder().decode(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

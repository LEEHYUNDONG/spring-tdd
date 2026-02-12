package com.demo.book.springtdd.seller.support;

import com.demo.book.springtdd.seller.adapter.in.dto.request.CreateSellerRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.request.IssueSellerTokenRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static com.demo.book.springtdd.testutils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.testutils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.testutils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;

public record TestFixture(
        TestRestTemplate client
) {

    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client);
    }

    private void ensureSuccessful(ResponseEntity<Void> response,
                                  Object request) {
        if (response.getStatusCode().is2xxSuccessful() == false) {
            String message = "Request with " + request + " failed with status code " + response.getStatusCode();
            throw new RuntimeException(message);
        }
    }

    public String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueSellerTokenRequest(email, password),
                AccessTokenCarrier.class);
        return requireNonNull(carrier).accessToken();
    }

    public static String createSellerThenIssueToken(
            TestFixture fixture) {
        String email = generateEmail();
        String password = generatePassword();
        String contactEmail = generateEmail();
        fixture.createSeller(email, generateUsername(), password, contactEmail);
        return fixture.issueSellerToken(email, password);
    }

    private void setSellerDefaultAuthorization(String email, String password) {
        String token = issueSellerToken(email, password);
        setDefaultAuthorization(token);
    }

    private void setDefaultAuthorization(String token) {
        RestTemplate template = client.getRestTemplate();
        template.getInterceptors().addFirst((request, body, execution) -> {
            if (request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
    }

    public void createSeller(String email, String username, String password, String contactEmail) {
        var command = new CreateSellerRequest(email, username, password, contactEmail);
        ensureSuccessful(
                client.postForEntity("/seller/signUp", command, Void.class),
                command);
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        String contactEmail = generateEmail();
        createSeller(email, generateUsername(), password, contactEmail);
        setSellerDefaultAuthorization(email, password);
    }

    // JWT token validation methods
    public Claims parseTokenClaims(String token) {
        String secret = "c2f3e8b4-9d4b-4a6e-8f3e-2d7c9a1b5e6f";
        return Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseTokenClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public String getTokenSubject(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getSubject();
    }

    public String getTokenScope(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.get("scp", String.class);
    }

    public String getTokenEmail(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.get("email", String.class);
    }

    public String getTokenIssuer(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getIssuer();
    }

    public Date getTokenExpiration(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getExpiration();
    }

    public Date getTokenIssuedAt(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getIssuedAt();
    }

    public String getTokenJti(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getId();
    }

    // Create expired token for testing
    public String createExpiredToken(String email, String password, String userType) {
        String secret = "c2f3e8b4-9d4b-4a6e-8f3e-2d7c9a1b5e6f";
        Instant now = Instant.now();
        Instant expiration = now.minus(1, ChronoUnit.HOURS);

        return Jwts
                .builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString())
                .setIssuer("spring-tdd-book")
                .setSubject("test-user-id")
                .setIssuedAt(Date.from(now.minus(2, ChronoUnit.HOURS)))
                .setExpiration(Date.from(expiration))
                .claim("scp", userType)
                .claim("email", email)
                .signWith(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .compact();
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }
}

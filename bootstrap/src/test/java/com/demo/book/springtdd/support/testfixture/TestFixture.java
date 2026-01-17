package com.demo.book.springtdd.support.testfixture;

import com.demo.book.springtdd.seller.adapter.in.dto.command.CreateSellerCommand;
import com.demo.book.springtdd.shopper.adapter.in.dto.command.CreateShopperCommand;
import com.demo.book.springtdd.product.adapter.in.dto.command.RegisterProductCommand;
import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import com.demo.book.springtdd.shopper.adapter.in.dto.query.IssueShopperToken;
import com.demo.book.springtdd.seller.adapter.in.dto.query.IssueSellerToken;
import com.demo.book.springtdd.seller.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.product.adapter.in.dto.result.PageCarrier;
import com.demo.book.springtdd.product.adapter.in.dto.view.ProductView;
import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.demo.book.springtdd.integration.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.integration.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.integration.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.integration.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.RequestEntity.get;

public record TestFixture(
        TestRestTemplate client,
        ProductsRepository productsRepository
) {

    public static TestFixture create(Environment environment, ProductsRepository productsRepository) {
        var client = new TestRestTemplate();
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client, productsRepository);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        ensureSuccessful(
                client.postForEntity("/shopper/signUp",
                        command, Void.class),
                command);
    }

    private void ensureSuccessful(ResponseEntity<Void> response,
                                  Object request) {
        if (response.getStatusCode().is2xxSuccessful() == false) {
            String message = "Request with " + request + " failed with status code " + response.getStatusCode();
            throw new RuntimeException(message);
        }
    }

    public String issueShopperToken(String email, String password) {
        var carrier = client.postForObject(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                com.demo.book.springtdd.shopper.adapter.in.dto.result.AccessTokenCarrier.class);
        return carrier.accessToken();
    }

    public String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class);
        return requireNonNull(carrier).accessToken();
    }

    public static String createShopperThenIssueToken(
            TestFixture fixture) {
        String email = generateEmail();
        String password = generatePassword();
        fixture.createShopper(email, generateUsername(), password);
        return fixture.issueShopperToken(email, password);
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
        setDefualtAuthorization(token);
    }

    public void setShopperDefaultAuthorization(String email, String password) {
        String token = issueShopperToken(email, password);
        setDefualtAuthorization(token);
    }

    private void setDefualtAuthorization(String token) {
        RestTemplate template = client.getRestTemplate();
        // interceptor를 추가하여 요청에 Authorization 헤더를 추가
        template.getInterceptors().addFirst((request, body, execution) -> {
            if (request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
    }

    public void createSeller(String email, String username, String password, String contactEmail) {
        var command = new CreateSellerCommand(email, username, password, contactEmail);
        // Ensure the command is successful
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

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperDefaultAuthorization(email, password);
    }

    // JWT 토큰 검증 메서드들
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

    // 만료된 토큰 생성 (테스트용)
    public String createExpiredToken(String email, String password, String userType) {
        String secret = "c2f3e8b4-9d4b-4a6e-8f3e-2d7c9a1b5e6f";
        Instant now = Instant.now();
        Instant expiration = now.minus(1, ChronoUnit.HOURS); // 1시간 전에 만료

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

    public String createProductForSellerAndGetLocation() {
        var command = generateRegisterProductCommand();
        ResponseEntity<Void> response = client.postForEntity("/seller/products", command, Void.class);
        return requireNonNull(response.getHeaders().getLocation()).toString();
    }

    public UUID registerProduct() {
        RegisterProductCommand command = generateRegisterProductCommand();
        return registerProduct(command);
    }

    public UUID registerProduct(RegisterProductCommand command) {

        ResponseEntity<Void> response = client.postForEntity("/seller/products", command, Void.class);

        URI location = response.getHeaders().getLocation();

        String path = requireNonNull(location).getPath();
        String id = path.substring("/seller/procucts/".length());
        return UUID.fromString(id);
    }

    public List<UUID> registerProducts() {
        return List.of(registerProduct(), registerProduct(), registerProduct());
    }

    public List<UUID> registerProducts(int count) {
        List<UUID> productIds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            productIds.add(registerProduct());
        }
        return productIds;
    }

    public void deleteAllProducts() {
        productsRepository.deleteAll();
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }

    public String consumeProductPage() {
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );
        return requireNonNull(response.getBody()).contunuationToken();
    }

    public String consumeTwoProductPages() {
        String token = consumeProductPage();
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                get("/shopper/products?continuationToken=" + token).build(),
                new ParameterizedTypeReference<>() {
                }
        );
        return requireNonNull(response.getBody()).contunuationToken();
    }
}

package com.demo.book.springtdd.testfixture;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.query.IssueShopperToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.view.ProductView;
import com.demo.book.springtdd.view.SellerMeView;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
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
        client.postForEntity("/shopper/signUp",
                command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                AccessTokenCarrier.class);
        return carrier.accessToken();
    }


    public static String createShopperThenIssueToken(
            TestFixture fixture) {
        String email = generateEmail();
        String password = generatePassword();
        fixture.createShopper(email, generateUsername(), password);
        return fixture.issueShopperToken(email, password);
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
            if(request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
    }

    private String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueShopperToken(email, password),
                AccessTokenCarrier.class);
        return requireNonNull(carrier).accessToken();
    }

    private void createSeller(String email, String password) {
        var command = new CreateShopperCommand(email, generateUsername(), password);
        client.postForEntity("/seller/signUp", command, Void.class);
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createSeller(email, password);
        setSellerDefaultAuthorization(email, password);
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperDefaultAuthorization(email, password);
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

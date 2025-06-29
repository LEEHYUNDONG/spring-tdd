package com.demo.book.springtdd.testfixture;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.query.IssueShopperToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;

public record TestFixture(TestRestTemplate client) {

    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client);
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
        var command = generateRegisterProductCommand();
        ResponseEntity<Void> response = client.postForEntity("/seller/products", command, Void.class);

        URI location = response.getHeaders().getLocation();

        String path = requireNonNull(location).getPath();
        String id = path.substring("/seller/procucts/".length());
        return UUID.fromString(id);


    }
}

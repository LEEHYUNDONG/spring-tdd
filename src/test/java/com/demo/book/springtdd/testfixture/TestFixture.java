package com.demo.book.springtdd.testfixture;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.query.IssueShopperToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;

public record TestFixture(TestRestTemplate client) {
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

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        RestTemplate template = client.getRestTemplate();
        // interceptor를 추가하여 요청에 Authorization 헤더를 추가
        template.getInterceptors().add((request, body, execution) -> {
            if(request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
    }
}

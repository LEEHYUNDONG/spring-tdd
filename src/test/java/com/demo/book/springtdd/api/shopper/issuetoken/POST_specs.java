package com.demo.book.springtdd.api.shopper.issuetoken;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.query.IssueShopperToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import com.demo.book.springtdd.utils.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.JwtAssertions.conformsToJwtFormat;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("POST /shopper/issueToken")
public class POST_specs {
    @Test
    void 올바르게_요청하면_200_OK_상태코드와_접근토큰을_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(generateEmail(),
                generateUsername(),
                generatePassword());

        //act
        client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        ResponseEntity<String> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(
                        command.email(),
                        command.password()
                ),
                String.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(generateEmail(),
                generateUsername(),
                generatePassword());

        //act
        client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(
                        command.email(),
                        command.password()
                ),
                AccessTokenCarrier.class
        );

        String actual = requireNonNull(response.getBody()).accessToken();
        //assert
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소로_요청하면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(generateEmail(),
                generateUsername(),
                generatePassword());

        //act
        ResponseEntity<?> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(
                        command.email(),
                        command.password()
                ),
                Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호로_요청하면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(generateEmail(),
                generateUsername(),
                generatePassword());

        //act
        client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(
                        command.email(),
                        "wrong-password"
                ),
                Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}

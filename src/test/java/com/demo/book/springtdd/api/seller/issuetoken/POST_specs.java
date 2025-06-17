package com.demo.book.springtdd.api.seller.issuetoken;

import com.demo.book.springtdd.SpringTddBookApplication;
import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.query.IssueSellerToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.seller.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.seller.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.seller.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        classes = SpringTddBookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestRestTemplate client){
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/issueToken", new CreateSellerCommand(
                email,
                generateUsername(),
                password
        ), Void.class);
        
        //act
        var response = client.postForEntity("/seller/issueToken", new IssueSellerToken(
                email,
                password
        ), Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 올바르게_요청하면_접근_토큰을_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/issueToken", new CreateSellerCommand(
                email,
                generateUsername(),
                password
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerToken(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isNotNull();
    }

}

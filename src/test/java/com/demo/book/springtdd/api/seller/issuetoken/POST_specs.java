package com.demo.book.springtdd.api.seller.issuetoken;

import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.query.IssueSellerToken;
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
@DisplayName("POST /seller/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestRestTemplate client){
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerCommand(
                email,
                generateUsername(),
                password,
                generateEmail()
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
        client.postForEntity("/seller/signUp", new CreateSellerCommand(
                email,
                generateUsername(),
                password,
                generateEmail()
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

    @Test
    void 접근_토큰은_JWT_형식을_따른다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerCommand(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerToken(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소로_요청하면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ){
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerCommand(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        // act
        ResponseEntity<Void> response = client.postForEntity("/seller/issueToken", new IssueSellerToken(
                generateEmail(),
                password
        ), Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호로_요청하면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ){
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerCommand(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        // act
        String wrongPassword = generatePassword();
        ResponseEntity<Void> response = client.postForEntity("/seller/issueToken", new IssueSellerToken(
                email,
                wrongPassword
        ), Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }



}

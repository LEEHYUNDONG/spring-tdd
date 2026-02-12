package com.demo.book.springtdd.seller.integration.issuetoken;

import com.demo.book.springtdd.seller.adapter.in.dto.request.CreateSellerRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.request.IssueSellerTokenRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.seller.support.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.testutils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.seller.support.JwtAssertions.*;
import static com.demo.book.springtdd.testutils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.testutils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import io.jsonwebtoken.Jwts;

@ApiTest
@DisplayName("POST /seller/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestRestTemplate client){
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        var response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
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
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        assertThat(response.getBody()).isNotNull();
        assertThat(requireNonNull(response.getBody()).accessToken()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(requireNonNull(response.getBody()).accessToken());
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 접근_토큰은_올바른_알고리즘을_사용한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidAlgorithm());
    }

    @Test
    void 접근_토큰은_만료_시간을_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidExpiration());
    }

    @Test
    void 접근_토큰은_24시간_후_만료된다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(expiresInHours(24));
    }

    @Test
    void 접근_토큰은_올바른_발행자를_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidIssuer("spring-tdd-book"));
    }

    @Test
    void 접근_토큰은_발행_시간을_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidIssuedAt());
    }

    @Test
    void 접근_토큰은_고유_ID를_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidJti());
    }

    @Test
    void 접근_토큰은_seller_스코프를_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidScope("seller"));
    }

    @Test
    void 접근_토큰은_사용자_이메일을_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidEmail());
    }

    @Test
    void 접근_토큰은_사용자_ID를_포함한다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(hasValidSubject());
    }

    @Test
    void 같은_사용자로_발행된_토큰들은_서로_다른_JTI를_가진다(@Autowired TestRestTemplate client) {
        //arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        //act
        ResponseEntity<AccessTokenCarrier> response1 = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        ResponseEntity<AccessTokenCarrier> response2 = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                password
        ), AccessTokenCarrier.class);

        //assert
        String token1 = requireNonNull(response1.getBody().accessToken());
        String token2 = requireNonNull(response2.getBody().accessToken());

        // JTI가 서로 다른지 확인
        String jti1 = parseJti(token1);
        String jti2 = parseJti(token2);
        assertThat(jti1).isNotEqualTo(jti2);
    }

    private String parseJti(String token) {
        String secret = "c2f3e8b4-9d4b-4a6e-8f3e-2d7c9a1b5e6f";
        return Jwts.parserBuilder()
                .setSigningKey(new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    @Test
    void 존재하지_않는_이메일_주소로_요청하면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ){
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        // act
        ResponseEntity<Void> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
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
        client.postForEntity("/seller/signUp", new CreateSellerRequest(
                email,
                generateUsername(),
                password,
                generateEmail()
        ), Void.class);

        // act
        String wrongPassword = generatePassword();
        ResponseEntity<Void> response = client.postForEntity("/seller/issueToken", new IssueSellerTokenRequest(
                email,
                wrongPassword
        ), Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}

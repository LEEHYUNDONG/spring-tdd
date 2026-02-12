package com.demo.book.springtdd.seller.integration.me;

import com.demo.book.springtdd.seller.adapter.in.dto.request.CreateSellerRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.request.IssueSellerTokenRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.seller.support.TestFixture;
import com.demo.book.springtdd.seller.support.ApiTest;
import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.testutils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.testutils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.testutils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.get;

@ApiTest
@DisplayName("GET /seller/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK__상태코드를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        var seller = new CreateSellerRequest(
                email,
                username,
                password,
                generateEmail()
        );
        client.postForEntity("/seller/signUp"
                , seller, Void.class);

        AccessTokenCarrier carrier = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(email, password),
                AccessTokenCarrier.class);

        String accessToken = carrier.accessToken();

        //act
        ResponseEntity<SellerMeView> response = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + accessToken)
                                .build(),
                        SellerMeView.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        var seller = new CreateSellerRequest(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail()
        );
        client.postForEntity("/seller/signUp"
                , seller, Void.class);
        //act
        ResponseEntity<SellerMeView> response = client
                .getForEntity("/seller/me", SellerMeView.class);
        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 서로_다른_판매자의_식별자는_서로_다르다(@Autowired TestRestTemplate client) {
        // arrange
        var seller1 = new CreateSellerRequest(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail()
        );
        client.postForEntity("/seller/signUp", seller1, Void.class);

        var seller2 = new CreateSellerRequest(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail()
        );
        client.postForEntity("/seller/signUp", seller2, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(seller1.email(), seller1.password()),
                AccessTokenCarrier.class);
        AccessTokenCarrier carrier2 = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(seller2.email(), seller2.password()),
                AccessTokenCarrier.class);

        //act
        ResponseEntity<SellerMeView> response1 = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + carrier1.accessToken())
                                .build(),
                        SellerMeView.class);

        ResponseEntity<SellerMeView> response2 = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + carrier2.accessToken())
                                .build(),
                        SellerMeView.class);

        //assert
        assertThat(requireNonNull(response1.getBody().id())).isNotEqualTo(requireNonNull(response2.getBody().id()));
    }

    @Test
    void 같은_판매자의_식별자는_항상_같다(@Autowired TestRestTemplate client) {
        // arrange
        var seller = new CreateSellerRequest(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail()
        );

        client.postForEntity("/seller/signUp", seller, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(seller.email(), seller.password()),
                AccessTokenCarrier.class);
        AccessTokenCarrier carrier2 = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(seller.email(), seller.password()),
                AccessTokenCarrier.class);

        //act
        ResponseEntity<SellerMeView> response1 = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + carrier1.accessToken())
                                .build(),
                        SellerMeView.class);

        ResponseEntity<SellerMeView> response2 = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + carrier2.accessToken())
                                .build(),
                        SellerMeView.class);

        //assert
        assertThat(requireNonNull(response1.getBody().id()))
                .isEqualTo(requireNonNull(response2.getBody().id()));
    }

    @Test
    void 판매자의_기본_정보가_올바르게_설정된다(@Autowired TestRestTemplate client) {
        // arrange
        var seller = new CreateSellerRequest(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail()

        );


        client.postForEntity("/seller/signUp", seller, Void.class);

        AccessTokenCarrier carrier = client.postForObject("/seller/issueToken",
                new IssueSellerTokenRequest(seller.email(), seller.password()),
                AccessTokenCarrier.class);

        //act
        ResponseEntity<SellerMeView> response = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + carrier.accessToken())
                                .build(),
                        SellerMeView.class);

        //assert
        assertThat(response.getBody().email()).isEqualTo(seller.email());
        assertThat(response.getBody().username()).isEqualTo(seller.username());
    }

    @Test
    void 문의_이메일_주소를_올바르게_설정한다(
            @Autowired TestFixture fixture
    ) {
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        String contactEmail = generateEmail();

        fixture.createSeller(
                email, username, password, contactEmail
        );

        AccessTokenCarrier carrier = fixture.client().postForObject(
                "/seller/issueToken",
                new IssueSellerTokenRequest(email, password),
                AccessTokenCarrier.class
        );
        String token = carrier.accessToken();

        //act
        ResponseEntity<SellerMeView> response = fixture.client().exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token)
                        .build(),
                SellerMeView.class
        );


        //assert
        SellerMeView actual = requireNonNull(response.getBody());
        assertThat(actual.contactEmail()).isEqualTo(contactEmail);
    }

    @Test
    void 만료된_토큰으로_요청하면_401_Unauthorized_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createSeller(email, generateUsername(), password, generateEmail());

        // 만료된 토큰 생성
        String expiredToken = fixture.createExpiredToken(email, password, "seller");

        // act
        ResponseEntity<Void> response = fixture.client().exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + expiredToken)
                        .build(),
                Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 잘못된_서명의_토큰으로_요청하면_401_Unauthorized_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // arrange
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // act
        ResponseEntity<Void> response = fixture.client().exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + invalidToken)
                        .build(),
                Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 토큰의_스코프가_올바르게_설정된다(@Autowired TestFixture fixture) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createSeller(email, generateUsername(), password, generateEmail());
        String token = fixture.issueSellerToken(email, password);

        // act & assert
        String scope = fixture.getTokenScope(token);
        assertThat(scope).isEqualTo("seller");
    }

    @Test
    void 토큰의_이메일이_올바르게_설정된다(@Autowired TestFixture fixture) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createSeller(email, generateUsername(), password, generateEmail());
        String token = fixture.issueSellerToken(email, password);

        // act & assert
        String tokenEmail = fixture.getTokenEmail(token);
        assertThat(tokenEmail).isEqualTo(email);
    }

    @Test
    void 토큰의_발행자가_올바르게_설정된다(@Autowired TestFixture fixture) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createSeller(email, generateUsername(), password, generateEmail());
        String token = fixture.issueSellerToken(email, password);

        // act & assert
        String issuer = fixture.getTokenIssuer(token);
        assertThat(issuer).isEqualTo("spring-tdd-book");
    }

}

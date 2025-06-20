package com.demo.book.springtdd.api.seller.me;

import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.query.IssueSellerToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
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
        var seller = new CreateSellerCommand(
                email,
                username,
                password
        );
        client.postForEntity("/seller/signUp"
                , seller, Void.class);

        AccessTokenCarrier carrier = client.postForObject("/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class);

        String accessToken = carrier.accessToken();
        System.out.println("accessToken = " + accessToken);

        //act
        ResponseEntity<SellerMeView> response = client
                .exchange(get("/seller/me")
                                .header("Authorization", "Bearer " + accessToken)
                                .build(),
                        SellerMeView.class);

        //assert
        System.out.println(response.toString());
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        var seller = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
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
        var seller1 = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );
        client.postForEntity("/seller/signUp", seller1, Void.class);

        var seller2 = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );
        client.postForEntity("/seller/signUp", seller2, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller1.email(), seller1.password()),
                AccessTokenCarrier.class);
        AccessTokenCarrier carrier2 = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller2.email(), seller2.password()),
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
        var seller = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );

        client.postForEntity("/seller/signUp", seller, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller.email(), seller.password()),
                AccessTokenCarrier.class);
        AccessTokenCarrier carrier2 = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller.email(), seller.password()),
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
        var seller = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );


        client.postForEntity("/seller/signUp", seller, Void.class);

        AccessTokenCarrier carrier = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller.email(), seller.password()),
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
}

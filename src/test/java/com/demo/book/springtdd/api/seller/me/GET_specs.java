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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("GET /seller/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK__상태코드를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        var seller = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword()
        );
        client.postForEntity("/seller/signUp"
                , seller, Void.class);

        AccessTokenCarrier carrier = client.postForObject("/seller/issueToken",
                new IssueSellerToken(seller.email(), seller.password()),
                AccessTokenCarrier.class);

        String accessToken = carrier.accessToken();

        //act
        ResponseEntity<SellerMeView> authorization = client.exchange(RequestEntity.get("/seller/me").header("Authorization", "Bearer" + accessToken).build(), SellerMeView.class);

        //assert
        assertThat(authorization.getStatusCode().value()).isEqualTo(200);

    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(@Autowired TestRestTemplate client) {

    }
}

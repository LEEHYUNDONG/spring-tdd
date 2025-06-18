package com.demo.book.springtdd.api.shopper.signup;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.utils.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("POST /api/shopper/signUp")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        //arrange
        var command = new CreateShopperCommand(generateEmail(),
                generateUsername(),
                generatePassword());

        //act
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(
                null,
                generateUsername(),
                generatePassword()
        );

        //act
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "user@domain",
            "user@.com",
            "@domain.com",
            "user@domain..com"
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(
            String invalidEmail,
            @Autowired TestRestTemplate client
    ) {
        //arrange
        var command = new CreateShopperCommand(
                invalidEmail,
                generateUsername(),
                generatePassword()
        );

        //act
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}

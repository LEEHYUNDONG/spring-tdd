package com.demo.book.springtdd.api.shopper.signup;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.domain.SellerRepository;
import com.demo.book.springtdd.domain.ShopperRepository;
import com.demo.book.springtdd.utils.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("POST /shopper/signUp")
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

    @Test
    void username_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(
            @Autowired TestRestTemplate client
    ){
        //arrange
        var command = new CreateShopperCommand(
                generateEmail(),
                "invalid-username!",
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
            "us", // 2글자
            "aa!",
            "user name", // 공백 포함
            "user@name", // 특수문자 포함
            ""
    })
    void username_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(String invalidUsername, @Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(
                generateEmail(),
                invalidUsername,
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
            "short", // 4글자
            "validUsername",
            "user123",
            "username_123"
    })
    void username_속성이_올바른_형식을_따르면_204_No_Content_상태코드를_반환한다(String validUsername, @Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateShopperCommand(
                generateEmail(),
                validUsername,
                generatePassword()
        );

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
    void password_속성이_올바른형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        String email = generateEmail();
        String password = null;


        // act
        ResponseEntity<Void> response = client.postForEntity("/shopper/signUp", new CreateShopperCommand(
                email,
                generateUsername(),
                password
        ), Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("com.demo.book.springtdd.api.utils.TestDatasource#invalidPasswords")
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(String invalidPassword, @Autowired TestRestTemplate client) {
        // arrange
        String email = generateEmail();
        String username = generateUsername();

        // act
        ResponseEntity<Void> response = client.postForEntity("/shopper/signUp", new CreateShopperCommand(
                email,
                username,
                invalidPassword
        ), Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }

    @Test
    void email_속성에_이미_존재하는_이메일주소가_지정되면_400_Bad_Request를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var existingEmail = generateEmail();
        var command = new CreateShopperCommand(
                existingEmail,
                generateUsername(),
                generatePassword()
        );
        // 먼저 기존 이메일로 사용자를 생성
        client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
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

    @Test
    void username_속성에_이미_존재하는_사용자이름이_지정되면_400_Bad_Request를_반환한다(@Autowired TestRestTemplate client) {
        // arrange
        var existingUsername = generateUsername();
        var command = new CreateShopperCommand(
                generateEmail(),
                existingUsername,
                generatePassword()
        );
        // 먼저 기존 사용자 이름으로 사용자를 생성
        client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );

        // act
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );
        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(@Autowired PasswordEncoder passwordEncoder, @Autowired ShopperRepository shopperRepository, @Autowired TestRestTemplate client) {
        // arrange
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        var command = new CreateShopperCommand(email, username, password);

        // act
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/signUp",
                command,
                Void.class
        );
        // 저장된 사용자 조회
        var savedShopper = shopperRepository.findByEmail(email)
                .orElseThrow();

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(passwordEncoder.matches(
                command.password(),
                savedShopper.getHashedPassword()
        )).isTrue();

    }

}

package com.demo.book.springtdd.api.seller.singup;

import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import com.demo.book.springtdd.utils.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ApiTest
@DisplayName("POST /seller/signUp")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword(),
                generateEmail());

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateSellerCommand(null, generateUsername(), "password", generateEmail());

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "invalid-email@",
            "invalid-email@test.",
            "invalid-email@test",
            "invalid-email@.com"
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(String email, @Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateSellerCommand(email, generateUsername(), "password", generateEmail());

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate testRestTemplate) {
        //arrange
        //arrange
        var command = new CreateSellerCommand(generateEmail(), null, "password", generateEmail());

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "ab",
            "a",
            "1",
            "user name with spaces",
            "user@name",
            "user!name",
            "us",
            "u",
            "123456789012345678901123!!123", // 21 characters
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_bad_request_상태를_반환한다(String username, @Autowired TestRestTemplate testRestTemplate) {
        //arrange
        var command = new CreateSellerCommand(generateEmail(), username, "password", generateEmail());

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate testRestTemplate) {
        // argange
        var command = new CreateSellerCommand(
                generateEmail(),
                "test01",
                null,
                generateEmail());

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234",
            "1",
            "a",
            "!"
    })
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(String password, @Autowired TestRestTemplate testRestTemplate) {
        // arrange
        var command = new CreateSellerCommand("test@test.com", "test01", password, generateEmail());

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void email_속성에_이미_존재하는_이메일주소가_지정되면_400_Bad_Request를_반환한다(@Autowired TestRestTemplate testRestTemplate) {
        //arrange
        var command = new CreateSellerCommand("test@test.com", "test01", "password!123", generateEmail());
        testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성에_이미_존재하는_사용자이름이_지정되면_400_Bad_Request를_반환한다(@Autowired TestRestTemplate testRestTemplate) {
        //arrange
        String username = generateUsername();
        var command = new CreateSellerCommand(generateEmail(), username, "password!123", generateEmail());
        testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", new CreateSellerCommand(generateEmail(), username, "anotherPassword!123", generateEmail()), Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(
            @Autowired TestRestTemplate testRestTemplate,
            @Autowired SellerRepository sellerRepository,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        //arrange
        var command = new CreateSellerCommand(generateEmail(), generateUsername(), generatePassword(), generateEmail());

        //act
        testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        Seller seller = sellerRepository
                .findAll()
                .stream()
                .filter(x -> x.getEmail().equals(command.email()))
                .findFirst()
                .orElseThrow();

        String actual = seller.getHashedPassword();

        assertThat(actual).isNotNull();
        assertThat(passwordEncoder.matches(command.password(), actual)).isTrue();
    }

}

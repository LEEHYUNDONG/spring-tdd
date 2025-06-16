package com.demo.book.springtdd.api.seller.singup;

import com.demo.book.springtdd.SpringTddBookApplication;
import com.demo.book.springtdd.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(
        classes = SpringTddBookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/signUp")
public class POST_specs {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(@Autowired TestRestTemplate client){
        //arrange
        var command = new CreateSellerCommand("seller@test.com", "seller", "password123!");

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //arrange
        var command = new CreateSellerCommand(null, "seller", "password");

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
        var command = new CreateSellerCommand(email, "seller", "password");

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate testRestTemplate) {
        //arrange
        //arrange
        var command = new CreateSellerCommand("test@test.com", null, "password");

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
            "123456789012345678901", // 21 characters
            "user_name_with_more_than_twenty_characters"
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_bad_request_상태를_반환한다(String username, @Autowired TestRestTemplate testRestTemplate) {
        //arrange
        var command = new CreateSellerCommand("test@test.com", username, "password");

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(){
        // argange
        var command = new CreateSellerCommand("test@test.com", "test01", null);

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
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태를_반환한다(String password, @Autowired TestRestTemplate testRestTemplate){
        // arrange
        var command = new CreateSellerCommand("test@test.com", "test01", password);

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void email_속성에_이미_존재하는_이메일주소가_지정되면_400_Bad_Request를_반환한다() {
        //arrange
        var command = new CreateSellerCommand("test@test.com", "test01", "password!123");

        //act
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}

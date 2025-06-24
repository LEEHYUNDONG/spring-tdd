package com.demo.book.springtdd.api.shopper.me;

import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.ShopperMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
import static com.demo.book.springtdd.api.utils.PasswordGenerator.generatePassword;
import static com.demo.book.springtdd.api.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.get;

@ApiTest
@DisplayName("GET /shopper/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        fixture.createShopper(email, generateUsername(), password);
        String token = fixture.issueShopperToken(email, password);

        // Act
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
                get("/shopper/me")
                        .header("Authorization", "Bearer " + token)
                        .build(),
                ShopperMeView.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void 서로_다른_판매자의_식별자는_서로_다르다() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void 같은_판매자의_식별자는_항상_같다() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void 구매자의_기본_정보가_올바르게_설정된다() {
        // Arrange

        // Act

        // Assert
    }
}

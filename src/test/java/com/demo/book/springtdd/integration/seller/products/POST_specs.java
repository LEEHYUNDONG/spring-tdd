package com.demo.book.springtdd.integration.seller.products;

import com.demo.book.springtdd.support.testfixture.TestFixture;
import com.demo.book.springtdd.support.utils.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.UUID;
import java.util.function.Predicate;

import static com.demo.book.springtdd.integration.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("POST /seller/products")
public class POST_specs {

    @Test
    void 올바르게_요청하면_201_Created_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        var command = generateRegisterProductCommand();

        // Act
        var response = fixture.client().postForEntity("/seller/products", command, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);

    }

    @Test
    void 비판매자_요청시_403_Forbidden_반환(@Autowired TestFixture fixture){
        // Arrange
        fixture.createShopperThenSetAsDefaultUser();
        var command = generateRegisterProductCommand();

        // Act
        var response = fixture.client().postForEntity("/seller/products", command, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-uri",
            "http://",
            "://missing-scheme.com",
    })
    void imageUri_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다( String invalidUri, @Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        var command = generateRegisterProductCommand(invalidUri);

        // Act
        var response = fixture.client().postForEntity("/seller/products", command, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 올바르게_요청하면_등록된_상품_정보에_접근하는_Location_헤더를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        var command = generateRegisterProductCommand();

        // Act
        var response = fixture.client().postForEntity("/seller/products", command, Void.class);

        // Assert
        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.getPath()).startsWith("/seller/products/").matches(
                endsWithUUID()
        );
    }

    private Predicate<? super String> endsWithUUID() {
        return path -> {
            String[] segments = path.split("/");
            String lastSegment = segments[segments.length - 1];
            try {
                UUID.fromString(lastSegment);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        };
    }

}

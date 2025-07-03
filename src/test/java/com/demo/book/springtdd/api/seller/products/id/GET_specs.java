package com.demo.book.springtdd.api.seller.products.id;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.demo.book.springtdd.api.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.utils.ProductAssertions.isDerivedFrom;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("GET /seller/products")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        String location = fixture.createProductForSellerAndGetLocation();

        // Act
        var response = fixture.client().getForEntity(location, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자가_아닌_사용자가_요청하면_403_Forbidden_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        String location = fixture.createProductForSellerAndGetLocation();

        // 변경: 판매자가 아닌 사용자를 생성
        fixture.createShopperThenSetAsDefaultUser();

        // Act
        var response = fixture.client().getForEntity(location, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @Test
    void 존재하지_않는_상품_식별자로_요청하면_404_Not_Found_상태코드를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        fixture.createProductForSellerAndGetLocation(); // 실제로는 존재하는 상품을 생성

        // Act
        var response = fixture.client().getForEntity("/seller/products/" + UUID.randomUUID(), Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void 올바른_상품_식별자로_요청하면_상품_정보를_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = generateRegisterProductCommand();
        UUID id = fixture.registerProduct(command);

        // Act
        SellerProductView actual = fixture.client().getForObject("/seller/products/" + id, SellerProductView.class);

        // Assert
        assertThat(actual).satisfies(isDerivedFrom(command));
    }

    @Test
    void 상품_등록_시각을_올바르게_반환한다(@Autowired TestFixture fixture) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        LocalDateTime referenceTime = LocalDateTime.now(UTC);
        RegisterProductCommand command = generateRegisterProductCommand();
        UUID id = fixture.registerProduct(command);

        // Act
        SellerProductView actual = fixture.client().getForObject("/seller/products/" + id, SellerProductView.class);

        // Assert
        assertThat(actual.registeredAt())
                .isCloseTo(referenceTime, within(1, ChronoUnit.SECONDS)); // 1초 이내의 오차 허용
    }

}

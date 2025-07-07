package com.demo.book.springtdd.api.seller.products;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.ArrayCarrier;
import com.demo.book.springtdd.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.demo.book.springtdd.api.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.utils.ProductAssertions.isDerivedFrom;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.springframework.http.RequestEntity.get;

@ApiTest
@DisplayName("GET /seller/products")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response = fixture.client().exchange(
                get("/seller/products").build(),
                new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {
                }
        );
        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자가_등록한_상품_목록을_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();
        List<UUID> ids = fixture.registerProducts();

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response = fixture.client().exchange(
                get("/seller/products").build(),
                new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {

                }
        );

        //assert
        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.items())
                .extracting(SellerProductView::id)
                .containsAll(ids);
    }

    @Test
    void 다른_판매자가_등록한_상품이_포함되지_않는다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();
        UUID unexpected = fixture.registerProduct();

        // 다른 판매자 생성
        fixture.createSellerThenSetAsDefaultUser();
        fixture.registerProducts();

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response = fixture.client().exchange(
                get("/seller/products").build(),
                new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {
                }
        );
        //assert
        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.items())
                .extracting(SellerProductView::id)
                .doesNotContain(unexpected)
                .doesNotContainNull();
    }

    @Test
    void 상품_정보를_올바르게_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = generateRegisterProductCommand();
        fixture.registerProduct(command);

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {
                        }
                );

        //assert
        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView actual = requireNonNull(body).items()[0];
        assertThat(actual).satisfies(isDerivedFrom(command));

    }

    @Test
    void 상품_등록_시각을_올바르게_반환한다(
            @Autowired TestFixture fixture
    ){
        //arrange
        fixture.createSellerThenSetAsDefaultUser();
        LocalDateTime referenceTime = LocalDateTime.now(UTC);
        RegisterProductCommand command = generateRegisterProductCommand();
        fixture.registerProduct(command);

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {
                        }
                );

        //assert
        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView actual = requireNonNull(body).items()[0];
        assertThat(actual.registeredAt()).isCloseTo(referenceTime, within(1, ChronoUnit.SECONDS));

    }

    @Test
    void 상품_목록을_등록_시점_역순으로_정렬한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();
        fixture.registerProducts();

        //act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
                fixture.client().exchange(
                        get("/seller/products").build(),
                        new ParameterizedTypeReference<ArrayCarrier<SellerProductView>>() {
                        }
                );

        //assert
        assertThat(response.getBody().items())
                .extracting(SellerProductView::registeredAt)
                .isSortedAccordingTo(Comparator.reverseOrder()); // 역순 정렬 확인

    }

}

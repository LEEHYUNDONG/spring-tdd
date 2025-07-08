package com.demo.book.springtdd.api.shopper.products;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.ProductView;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.demo.book.springtdd.api.utils.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static com.demo.book.springtdd.utils.ProductAssertions.isViewDerivedFrom;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.get;

@ApiTest
@DisplayName("GET /shopper/products")
public class GET_specs {

    private static final int PAGE_SIZE = 10;

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createShopperThenSetAsDefaultUser();

        //act
        ResponseEntity<Object> response = fixture.client().exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자_접근토큰을_사용하면_403_FORBIDDEN_상태코드를_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.createSellerThenSetAsDefaultUser();

        //act
        ResponseEntity<Object> response = fixture.client().exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @Test
    void 첫_번째_페이지의_상품을_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.deleteAllProducts();

        fixture.createSellerThenSetAsDefaultUser();
        List<UUID> ids = fixture.registerProducts(PAGE_SIZE);
        fixture.createShopperThenSetAsDefaultUser();


        //act
        ResponseEntity<PageCarrier<ProductView>> response = fixture.client().exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        PageCarrier<ProductView> actual = response.getBody();
        assertThat(actual.items()).extracting(ProductView::id)
                .containsAll(ids);

    }

    @Test
    void 상품_목록을_등록_시점_역순으로_정렬한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.deleteAllProducts();
        fixture.createSellerThenSetAsDefaultUser();
        UUID id1 = fixture.registerProduct();
        UUID id2 = fixture.registerProduct();
        UUID id3 = fixture.registerProduct();
        fixture.createShopperThenSetAsDefaultUser();

        //act
        ResponseEntity<PageCarrier<ProductView>> response = fixture.client().exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        PageCarrier<ProductView> actual = response.getBody();
        assertThat(actual.items()).extracting(ProductView::id)
                .containsExactly(id3, id2, id1);
    }

    @Test
    void 상품_속성을_올바르게_반환한다(
            @Autowired TestFixture fixture
    ) {
        //arrange
        fixture.deleteAllProducts();
        fixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = generateRegisterProductCommand();
        fixture.registerProduct(command);
        fixture.createShopperThenSetAsDefaultUser();

        //act
        ResponseEntity<PageCarrier<ProductView>> response = fixture.client().exchange(
                get("/shopper/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ProductView actual = response.getBody().items()[0];
        assertThat(actual).satisfies(isViewDerivedFrom(command));

    }


}

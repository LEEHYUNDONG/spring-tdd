package com.demo.book.springtdd.api.seller.products;

import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.ArrayCarrier;
import com.demo.book.springtdd.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        List<UUID> ids =  fixture.registerProducts();

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
}

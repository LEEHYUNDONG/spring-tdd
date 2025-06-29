package com.demo.book.springtdd.api.seller.products.id;

import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("GET /seller/products/{id}")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_상태코드를_반환한다(
            @Autowired TestFixture testFixture
    ){
        // arrange
        testFixture.createSellerThenSetAsDefaultUser();
        UUID id = testFixture.registerProduct();

        //act
        ResponseEntity<?> response = testFixture.client().getForEntity(
                "/seller/products/" + id,
                SellerProductView.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}

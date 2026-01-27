package com.demo.book.springtdd.seller.integration.changecontactemail;

import com.demo.book.springtdd.seller.support.InvalidEmailSource;
import com.demo.book.springtdd.seller.adapter.in.dto.command.ChangeContactEmailCommand;
import com.demo.book.springtdd.seller.support.TestFixture;
import com.demo.book.springtdd.seller.support.ApiTest;
import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.testutils.EmailGenerator.generateEmail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApiTest
@DisplayName("POST /api/seller/changeContactEmail")
public class Post_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
            @Autowired TestFixture fixture
    ) {
        // arrange
        fixture.createSellerThenSetAsDefaultUser();
        String contactEmail = "example@example.com";
        var command = new ChangeContactEmailCommand(contactEmail);
        // act
        ResponseEntity<Void> response = fixture.client().postForEntity("/seller/changeContactEmail",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);

    }

    @ParameterizedTest
    @InvalidEmailSource
    void contactEmail이_비어있으면_400_Bad_Request_상태코드를_반환한다(
            String contactEmail,
            @Autowired TestFixture fixture
    ) {
        // arrange
        fixture.createSellerThenSetAsDefaultUser();
        var command = new ChangeContactEmailCommand(contactEmail);
        // act
        ResponseEntity<Void> response = fixture.client().postForEntity("/seller/changeContactEmail",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 문의_이메일_주소를_올바르게_변경한다(
            @Autowired TestFixture fixture
    ) {
        // arrange
        fixture.createSellerThenSetAsDefaultUser();
        String newContactEmail = generateEmail();

        // act
        ResponseEntity<Void> response = fixture.client().postForEntity("/seller/changeContactEmail",
                new ChangeContactEmailCommand(newContactEmail),
                Void.class
        );
        // assert
        SellerMeView seller = fixture.getSeller();
        assertThat(seller.contactEmail()).isEqualTo(newContactEmail);
    }
}

package com.demo.book.springtdd.api.seller.changecontactemail;

import com.demo.book.springtdd.api.utils.InvalidEmailSource;
import com.demo.book.springtdd.command.ChangeContactEmailCommand;
import com.demo.book.springtdd.testfixture.TestFixture;
import com.demo.book.springtdd.utils.ApiTest;
import com.demo.book.springtdd.view.SellerMeView;
import com.demo.book.springtdd.view.SellerView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.demo.book.springtdd.api.utils.EmailGenerator.generateEmail;
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

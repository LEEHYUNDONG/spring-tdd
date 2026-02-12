package com.demo.book.springtdd.seller.application.usecase;

import com.demo.book.springtdd.seller.application.port.in.ForChangingContactEmail;
import com.demo.book.springtdd.seller.application.port.in.command.ChangeContactEmailCommand;
import com.demo.book.springtdd.seller.application.port.out.ReadSellerPort;
import com.demo.book.springtdd.seller.application.port.out.UpdateSellerPort;
import com.demo.book.springtdd.seller.domain.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeContactEmailUsecase implements ForChangingContactEmail {

    private final ReadSellerPort readSellerPort;
    private final UpdateSellerPort updateSellerPort;

    @Override
    public void changeContactEmail(ChangeContactEmailCommand command) {
        Seller seller = readSellerPort.findById(command.sellerId());
        seller.setContactEmail(command.contactEmail());
        updateSellerPort.update(seller);
    }
}

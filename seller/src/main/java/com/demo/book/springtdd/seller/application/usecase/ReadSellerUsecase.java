package com.demo.book.springtdd.seller.application.usecase;

import com.demo.book.springtdd.seller.application.port.in.ForReadingSeller;
import com.demo.book.springtdd.seller.application.port.in.query.ReadSellerQuery;
import com.demo.book.springtdd.seller.application.port.in.result.SellerInfo;
import com.demo.book.springtdd.seller.application.port.out.ReadSellerPort;
import com.demo.book.springtdd.seller.domain.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadSellerUsecase implements ForReadingSeller {

    private final ReadSellerPort readSellerPort;

    @Override
    public SellerInfo read(ReadSellerQuery query) {
        Seller seller = readSellerPort.findById(query.sellerId());
        return new SellerInfo(
                seller.getId(),
                seller.getEmail(),
                seller.getUsername(),
                seller.getContactEmail()
        );
    }
}

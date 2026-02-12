package com.demo.book.springtdd.shopper.application.usecase;

import com.demo.book.springtdd.shopper.application.port.in.ForReadingShopper;
import com.demo.book.springtdd.shopper.application.port.in.query.ReadShopperQuery;
import com.demo.book.springtdd.shopper.application.port.in.result.ShopperInfo;
import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadShopperUsecase implements ForReadingShopper {

    private final ReadShopperPort readShopperPort;

    @Override
    public ShopperInfo read(ReadShopperQuery query) {
        Shopper shopper = readShopperPort.findById(query.shopperId());
        return new ShopperInfo(
                shopper.getId(),
                shopper.getEmail(),
                shopper.getUsername()
        );
    }
}

package com.demo.book.springtdd.shopper.application.usecase;

import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.domain.*;
import com.demo.book.springtdd.shopper.application.port.in.ShopperUsecase;
import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopperService implements ShopperUsecase {

    private final PasswordEncoder passwordEncoder;
    private final CreateShopperPort createShopperPort;
    private final ReadShopperPort readShopperPort;

    @Override
    public ShopperInfo read(ReadShopperQuery query) {
        Shopper shopper = readShopperPort.findById(query.shopperId())
                .orElseThrow(EntityNotFoundException::new);

        return new ShopperInfo(
                shopper.getId().getValue(),
                shopper.getEmail(),
                shopper.getUsername()
        );
    }

    @Override
    public ShopperId signUp(CreateShopperCommand command) {
        var shopper = Shopper.register(command, passwordEncoder);

        createShopperPort.create(shopper);

        return shopper.getId();
    }
}

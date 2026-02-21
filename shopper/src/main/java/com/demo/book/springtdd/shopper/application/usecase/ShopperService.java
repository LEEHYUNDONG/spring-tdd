package com.demo.book.springtdd.shopper.application.usecase;

import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;
import com.demo.book.springtdd.shopper.application.port.in.ShopperUsecase;
import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.shopper.domain.ReadShopperQuery;
import com.demo.book.springtdd.shopper.domain.Shopper;
import com.demo.book.springtdd.shopper.domain.ShopperInfo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
                shopper.getId(),
                shopper.getEmail(),
                shopper.getUsername()
        );
    }

    @Override
    public Shopper signUp(CreateShopperCommand command) {
        Shopper byEmail = readShopperPort.findByEmail(command.email());
        if (byEmail != null) {
            throw new DuplicateKeyException("Shopper with email already exists");
        }

        var shopper = Shopper.register(command, passwordEncoder);


        createShopperPort.create(shopper);

        return shopper;
    }
}

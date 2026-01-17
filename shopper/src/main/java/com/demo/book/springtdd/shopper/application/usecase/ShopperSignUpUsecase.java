package com.demo.book.springtdd.shopper.application.usecase;

import com.demo.book.springtdd.shopper.adapter.in.dto.command.CreateShopperCommand;
import com.demo.book.springtdd.shopper.application.port.in.ForCreatingShopper;
import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopperSignUpUsecase implements ForCreatingShopper {

    private final PasswordEncoder passwordEncoder;
    private final CreateShopperPort createShopperPort;

    @Override
    public void signUp(CreateShopperCommand command) {
        String hashedPassword = passwordEncoder.encode(command.password());
        UUID id = UUID.randomUUID();

        var shopper = new Shopper();
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(hashedPassword);
        shopper.setId(id);

        createShopperPort.create(shopper);
    }
}

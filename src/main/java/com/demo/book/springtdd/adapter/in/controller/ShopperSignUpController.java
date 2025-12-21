package com.demo.book.springtdd.adapter.in.controller;

import com.demo.book.springtdd.adapter.in.dto.command.CreateShopperCommand;
import com.demo.book.springtdd.domain.Shopper;
import com.demo.book.springtdd.adapter.out.persistence.repository.ShopperRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.UUID;

import static com.demo.book.springtdd.adapter.in.support.UserPropertyValidator.*;


@RestController
public record ShopperSignUpController(ShopperRepository shopperRepository,
                                      PasswordEncoder passwordEncoder) {

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if(!isValidCommand(command)) {
            return ResponseEntity.badRequest()
                    .build();
        }
        UUID id = UUID.randomUUID();
        Shopper shopper = new Shopper();

        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));
        shopper.setId(id);

        shopperRepository.save(shopper);

        return ResponseEntity.noContent()
                .build();
    }



    private boolean isValidCommand(CreateShopperCommand command) {
        return isEmailValid(command.email()) && isUsernameValid(command.username())
                && isPasswordValid(command.password());
    }

}

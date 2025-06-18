package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.CreateShopperCommand;
import com.demo.book.springtdd.domain.Shopper;
import com.demo.book.springtdd.domain.ShopperRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.util.Optional;

import static com.demo.book.springtdd.api.UserPropertyValidator.*;


@RestController
public record ShopperSignUpController(ShopperRepository shopperRepository, PasswordEncoder passwordEncoder) {

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if(!isValidCommand(command)) {
            return ResponseEntity.badRequest()
                    .build();
        }

        Shopper shopper = new Shopper();
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));

        shopperRepository.save(shopper);


        return ResponseEntity.noContent()
                .build();
    }

    private boolean isValidCommand(CreateShopperCommand command) {
        return isEmailValid(command.email()) && isUsernameValid(command.username())
                && isPasswordValid(command.password());
    }

}

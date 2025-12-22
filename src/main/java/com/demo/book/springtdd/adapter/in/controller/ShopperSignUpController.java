package com.demo.book.springtdd.adapter.in.controller;

import com.demo.book.springtdd.adapter.in.dto.command.CreateShopperCommand;
import com.demo.book.springtdd.application.port.in.ForCreatingShopper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.demo.book.springtdd.adapter.in.support.UserPropertyValidator.*;

@RestController
@RequiredArgsConstructor
public class ShopperSignUpController {

    private final ForCreatingShopper forCreatingShopper;

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if (isCommandNotValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        forCreatingShopper.signUp(command);

        return ResponseEntity.noContent().build();
    }

    public static boolean isCommandNotValid(CreateShopperCommand command) {
        return !isEmailValid(command.email()) ||
                !isUsernameValid(command.username()) ||
                !isPasswordValid(command.password());
    }
}


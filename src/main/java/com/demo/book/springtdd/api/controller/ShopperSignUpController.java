package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.CreateShopperCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public record ShopperSignUpController() {

    private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";
    private static final String passwordRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{8,}$";

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if(command.email() == null) {
            return ResponseEntity.badRequest()
                    .build();
        } else if (!command.email().matches(emailRegex)) {
            return ResponseEntity.badRequest()
                    .build();
        }
        return ResponseEntity.noContent()
                .build();
    }

}

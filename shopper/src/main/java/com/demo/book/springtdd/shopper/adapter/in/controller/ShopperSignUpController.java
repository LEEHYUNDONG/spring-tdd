package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.adapter.in.dto.request.CreateShopperRequest;
import com.demo.book.springtdd.shopper.application.port.in.ForCreatingShopper;
import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.demo.book.springtdd.support.UserPropertyValidator.*;

@RestController
@RequiredArgsConstructor
public class ShopperSignUpController {

    private final ForCreatingShopper forCreatingShopper;

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperRequest request) {
        if (isRequestNotValid(request)) {
            return ResponseEntity.badRequest().build();
        }

        var command = new CreateShopperCommand(
                request.email(),
                request.username(),
                request.password()
        );
        forCreatingShopper.signUp(command);

        return ResponseEntity.noContent().build();
    }

    public static boolean isRequestNotValid(CreateShopperRequest request) {
        return !isEmailValid(request.email()) ||
                !isUsernameValid(request.username()) ||
                !isPasswordValid(request.password());
    }
}

package com.demo.book.springtdd.seller.adapter.in.controller;

import com.demo.book.springtdd.seller.adapter.in.dto.request.CreateSellerRequest;
import com.demo.book.springtdd.seller.application.port.in.ForCreatingSeller;
import com.demo.book.springtdd.seller.application.port.in.command.CreateSellerCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.demo.book.springtdd.support.UserPropertyValidator.*;

@RequiredArgsConstructor
@RestController
public class SellerSignUpController {

    private final ForCreatingSeller forCreatingSeller;

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerRequest request) {
        if (isRequestNotValid(request)) {
            return ResponseEntity.badRequest().build();
        }

        var command = new CreateSellerCommand(
                request.email(),
                request.username(),
                request.password(),
                request.contactEmail()
        );
        forCreatingSeller.signUp(command);

        return ResponseEntity.noContent().build();
    }

    public static boolean isRequestNotValid(CreateSellerRequest request) {
        return !isEmailValid(request.email()) ||
                !isUsernameValid(request.username()) ||
                !isPasswordValid(request.password())
                || !isEmailValid(request.contactEmail());
    }
}

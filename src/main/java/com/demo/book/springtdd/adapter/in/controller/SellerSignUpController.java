package com.demo.book.springtdd.adapter.in.controller;

import com.demo.book.springtdd.adapter.in.dto.command.CreateSellerCommand;
import com.demo.book.springtdd.application.port.in.ForCreatingSeller;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.adapter.out.persistence.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.demo.book.springtdd.adapter.in.support.UserPropertyValidator.*;

@RequiredArgsConstructor
@RestController
public class SellerSignUpController {

    private final SellerRepository sellerRepository;

    private final ForCreatingSeller forCreatingSeller;

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        if (isCommandNotValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        forCreatingSeller.signUp(command);

        return ResponseEntity.noContent().build();
    }
    public static boolean isCommandNotValid(CreateSellerCommand command) {
        return !isEmailValid(command.email()) ||
                !isUsernameValid(command.username()) ||
                !isPasswordValid(command.password())
                || !isEmailValid(command.contactEmail());
    }
}

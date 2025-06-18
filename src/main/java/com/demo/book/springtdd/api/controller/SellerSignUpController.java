package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.api.UserPropertyValidator;
import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.demo.book.springtdd.api.UserPropertyValidator.*;

@RestController
public record SellerSignUpController(PasswordEncoder passwordEncoder,
                                     SellerRepository sellerRepository) {

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        if (isCommandNotValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        String hashedPassword = passwordEncoder.encode(command.password());
        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);
        sellerRepository.save(seller);

        return ResponseEntity.noContent().build();
    }

    public static boolean isCommandNotValid(CreateSellerCommand command) {
        return !isEmailValid(command.email()) ||
                !isUsernameValid(command.username()) ||
                !isPasswordValid(command.password());
    }
}

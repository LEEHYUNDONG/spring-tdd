package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.CreateSellerCommand;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController(SellerRepository sellerRepository) {

    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String usernameRegex = "^[a-zA-Z0-9_]{3,}$";
    private static final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        System.out.println("Received sign-up request: " + command);
        if (isCommandNotValid(command))
            return ResponseEntity.badRequest().build();

        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());

        System.out.println("Received sign-up request: " + command);
        try{
            sellerRepository.save(seller);
        } catch(DataIntegrityViolationException e){
            return ResponseEntity.badRequest().build();
        }


        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandNotValid(CreateSellerCommand command) {
        return isEmailValid(command.email()) ||
                isUsernameValid(command.username()) ||
                isPasswordValid(command.password());
    }

    private static boolean isEmailValid(String email) {
        return email == null || !email.matches(emailRegex);
    }

    private static boolean isUsernameValid(String username) {
        return username == null || !username.matches(usernameRegex);
    }

    private static boolean isPasswordValid(String password) {
        return password == null || !password.matches(passwordRegex);
    }

}

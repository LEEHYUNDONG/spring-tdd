package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.command.ChangeContactEmailCommand;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static com.demo.book.springtdd.api.UserPropertyValidator.isEmailValid;

@RestController
public record SellerChangeContactEmailController(
    SellerRepository sellerRepository
) {
    @PostMapping("/seller/changeContactEmail")
    ResponseEntity<?> changeContactEmail(
            @RequestBody ChangeContactEmailCommand command,
            Principal user
    ) {
        if (!isEmailValid(command.contactEmail())) {
            // Return a 400 Bad Request if the email is invalid
            return ResponseEntity.badRequest().build();
        }
        UUID id = UUID.fromString(user.getName());
        Seller seller = sellerRepository.findById(id).orElseThrow();
        seller.changeContactEmail(command.contactEmail());
        sellerRepository.save(seller);
        return ResponseEntity.noContent().build();
    }
}

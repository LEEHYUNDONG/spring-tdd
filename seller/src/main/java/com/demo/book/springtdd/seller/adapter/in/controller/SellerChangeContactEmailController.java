package com.demo.book.springtdd.seller.adapter.in.controller;

import com.demo.book.springtdd.seller.adapter.in.dto.command.ChangeContactEmailRequest;
import com.demo.book.springtdd.seller.application.port.in.ForChangingContactEmail;
import com.demo.book.springtdd.seller.application.port.in.command.ChangeContactEmailCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static com.demo.book.springtdd.support.UserPropertyValidator.isEmailValid;

@RestController
public record SellerChangeContactEmailController(
    ForChangingContactEmail forChangingContactEmail
) {
    @PostMapping("/seller/changeContactEmail")
    ResponseEntity<?> changeContactEmail(
            @RequestBody ChangeContactEmailRequest request,
            Principal user
    ) {
        if (!isEmailValid(request.contactEmail())) {
            return ResponseEntity.badRequest().build();
        }
        UUID id = UUID.fromString(user.getName());
        forChangingContactEmail.changeContactEmail(
                new ChangeContactEmailCommand(id, request.contactEmail())
        );
        return ResponseEntity.noContent().build();
    }
}

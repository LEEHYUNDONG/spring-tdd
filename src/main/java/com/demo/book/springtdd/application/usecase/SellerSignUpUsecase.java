package com.demo.book.springtdd.application.usecase;

import com.demo.book.springtdd.adapter.in.dto.command.CreateSellerCommand;
import com.demo.book.springtdd.application.port.in.ForCreatingSeller;
import com.demo.book.springtdd.application.port.out.CreateSellerPort;
import com.demo.book.springtdd.domain.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.demo.book.springtdd.adapter.in.support.UserPropertyValidator.*;
import static com.demo.book.springtdd.adapter.in.support.UserPropertyValidator.isEmailValid;

@Component
@RequiredArgsConstructor
public class SellerSignUpUsecase implements ForCreatingSeller {

    private final PasswordEncoder passwordEncoder;

    private final CreateSellerPort createSellerPort;

    @Override
    public void signUp(CreateSellerCommand command) {

        String hashedPassword = passwordEncoder.encode(command.password());
        UUID id = UUID.randomUUID();
        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);
        seller.setContactEmail(command.contactEmail());

        seller.setId(id);

        createSellerPort.create(seller);
    }

}

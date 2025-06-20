package com.demo.book.springtdd.api.controller;


import com.demo.book.springtdd.config.JwtKeyHolder;
import com.demo.book.springtdd.domain.SellerRepository;
import com.demo.book.springtdd.view.SellerMeView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController(SellerRepository sellerRepository) {

    @GetMapping("/seller/me")
    SellerMeView sellerMe(Principal user) {
        UUID id = UUID.fromString(user.getName());
        return sellerRepository.findById(id)
                .map(seller -> new SellerMeView(
                        seller.getId(),
                        seller.getEmail(),
                        seller.getUsername()
                ))
                .orElseThrow();
    }

}

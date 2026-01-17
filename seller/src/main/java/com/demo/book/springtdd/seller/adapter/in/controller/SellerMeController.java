package com.demo.book.springtdd.seller.adapter.in.controller;


import com.demo.book.springtdd.seller.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
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
                        seller.getUsername(),
                        seller.getContactEmail()
                ))
                .orElseThrow();
    }

}

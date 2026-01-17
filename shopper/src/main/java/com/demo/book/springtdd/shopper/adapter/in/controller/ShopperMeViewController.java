package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.domain.Shopper;
import com.demo.book.springtdd.shopper.adapter.out.persistence.repository.ShopperRepository;
import com.demo.book.springtdd.shopper.adapter.in.dto.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeViewController(ShopperRepository shopperRepository) {

    @GetMapping("/shopper/me")
    public ShopperMeView me(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        Shopper shopper = shopperRepository.findById(id).orElseThrow();

        return new ShopperMeView(shopper.getId(), shopper.getEmail(), shopper.getUsername());
    }

}

package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.domain.ShopperRepository;
import com.demo.book.springtdd.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeViewController(ShopperRepository shopperRepository) {

    @GetMapping("/shopper/me")
    public ShopperMeView me(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        return new ShopperMeView(id, null, null);
    }

}

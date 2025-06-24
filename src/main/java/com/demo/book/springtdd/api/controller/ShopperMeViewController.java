package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperMeViewController() {

    @GetMapping("/shopper/me")
    public ShopperMeView me() {
        return null;
    }

}

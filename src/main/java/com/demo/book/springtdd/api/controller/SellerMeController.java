package com.demo.book.springtdd.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerMeController() {

    @GetMapping("/seller/me")
    void sellerMe() {

    }
}

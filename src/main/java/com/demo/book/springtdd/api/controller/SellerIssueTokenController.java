package com.demo.book.springtdd.api.controller;


import com.demo.book.springtdd.result.AccessTokenCarrier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerIssueTokenController() {

    @PostMapping("/seller/issueToken")
    AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier("new");
    }
}

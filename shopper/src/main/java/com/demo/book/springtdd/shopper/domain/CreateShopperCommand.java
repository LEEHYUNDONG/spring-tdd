package com.demo.book.springtdd.shopper.domain;

public record CreateShopperCommand(String email, String username, String password) {
}

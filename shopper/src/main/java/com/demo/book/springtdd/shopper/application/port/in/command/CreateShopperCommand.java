package com.demo.book.springtdd.shopper.application.port.in.command;

public record CreateShopperCommand(String email, String username, String password) {
}

package com.demo.book.springtdd.command;

public record CreateShopperCommand(String email, String username, String password) {
}

package com.demo.book.springtdd.seller.application.port.in.command;

public record CreateSellerCommand(
        String email,
        String username,
        String password,
        String contactEmail
) {
}

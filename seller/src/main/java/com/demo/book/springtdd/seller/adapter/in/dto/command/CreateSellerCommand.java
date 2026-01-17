package com.demo.book.springtdd.seller.adapter.in.dto.command;

public record CreateSellerCommand(
        String email,
        String username,
        String password,
        String contactEmail
) {
}

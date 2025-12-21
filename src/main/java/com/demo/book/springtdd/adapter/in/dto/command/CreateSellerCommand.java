package com.demo.book.springtdd.adapter.in.dto.command;

public record CreateSellerCommand(
        String email,
        String username,
        String password,
        String contactEmail
) {
}

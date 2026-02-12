package com.demo.book.springtdd.seller.adapter.in.dto.request;

public record CreateSellerRequest(
        String email,
        String username,
        String password,
        String contactEmail
) {
}

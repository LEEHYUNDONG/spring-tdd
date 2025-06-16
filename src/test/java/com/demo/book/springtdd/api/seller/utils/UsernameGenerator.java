package com.demo.book.springtdd.api.seller.utils;


import java.util.UUID;

public class UsernameGenerator {

    public static String generateUsername() {
        return "username" + UUID.fromString(UUID.randomUUID().toString()).toString().replace("-", "");
    }
}

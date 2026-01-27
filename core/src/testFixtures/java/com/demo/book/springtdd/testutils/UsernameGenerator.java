package com.demo.book.springtdd.testutils;

import java.util.UUID;

public class UsernameGenerator {

    public static String generateUsername() {
        return "username" + UUID.fromString(UUID.randomUUID().toString()).toString().replace("-", "").substring(0, 10);
    }
}

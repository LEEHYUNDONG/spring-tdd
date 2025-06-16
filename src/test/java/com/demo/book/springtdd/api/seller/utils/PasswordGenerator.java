package com.demo.book.springtdd.api.seller.utils;


import java.util.UUID;

public class PasswordGenerator {
    public static String generatePassword() {
        return "password" + UUID.randomUUID();
    }
}

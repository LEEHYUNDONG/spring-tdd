package com.demo.book.springtdd.integration.utils;


import java.security.SecureRandom;

public class PasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static String generatePassword() {
        var mixture = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            mixture.append((char) (RANDOM.nextInt(26) + 'a')); // Lowercase letters
            mixture.append((char) (RANDOM.nextInt(26) + 'A')); // Uppercase letters
            mixture.append((char) (RANDOM.nextInt(10) + '0')); // Digits
        }
        return "password"+ mixture.toString();
    }
}

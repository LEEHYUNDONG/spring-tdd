package com.demo.book.springtdd.integration.utils;

import java.util.UUID;

public class EmailGenerator {

    public static String generateEmail() {
        return UUID.fromString(UUID.randomUUID().toString()).toString().replace("-", "") + "@test.com";
    }
}

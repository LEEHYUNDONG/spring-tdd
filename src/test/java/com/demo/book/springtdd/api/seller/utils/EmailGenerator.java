package com.demo.book.springtdd.api.seller.utils;

import java.util.UUID;

public class EmailGenerator {

    public static String generateEmail() {
        return UUID.randomUUID() + "@test.com";
    }
}

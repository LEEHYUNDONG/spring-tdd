package com.demo.book.springtdd.api.utils;

public class TestDatasource {

    public static String[] invalidPasswords() {
        return new String[]{
                "",
                "pass",
                "pass123",
                "12345678pass",
                "12345678",
                "123password45678",
        };
    };
}

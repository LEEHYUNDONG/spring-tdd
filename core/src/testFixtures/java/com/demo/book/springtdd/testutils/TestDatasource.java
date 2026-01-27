package com.demo.book.springtdd.testutils;

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
    }

    public static String[] invalidEmail() {
        return new String[]{
                null,
                "invalid-email",
                "invalid-email@",
                "invalid-email@test.",
                "invalid-email@test",
                "invalid-email@.com"
        };
    }
}

package com.demo.book.springtdd.utils;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

public class PasswordEncoderConfiguration {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 10;

    @Bean
    @Primary
    Pbkdf2PasswordEncoder testPasswordEncoder() {

        return new Pbkdf2PasswordEncoder(
                "",
                SALT_LENGTH,
                ITERATION_COUNT,
                PBKDF2WithHmacSHA256
        );
    }
}

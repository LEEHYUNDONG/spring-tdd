package com.demo.book.springtdd.api.utils;

import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@MethodSource("com.demo.book.springtdd.api.utils.TestDatasource#invalidEmail")
public @interface InvalidEmailSource {
}

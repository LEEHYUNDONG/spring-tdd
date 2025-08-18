package com.demo.book.springtdd.utils;

import com.demo.book.springtdd.SpringTddBookApplication;
import com.demo.book.springtdd.testfixture.TestFixtureConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootTest(
        classes = {SpringTddBookApplication.class,
                TestFixtureConfiguration.class,
                PasswordEncoderConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiTest {
}

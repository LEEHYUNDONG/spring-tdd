package com.demo.book.springtdd.seller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.demo.book.springtdd.seller",
        "com.demo.book.springtdd.exception",
        "com.demo.book.springtdd.infrastructure",
        "com.demo.book.springtdd.support"
})
public class SellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellerApplication.class, args);
    }
}

package com.demo.book.springtdd.shopper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.demo.book.springtdd.shopper",
        "com.demo.book.springtdd.exception",
        "com.demo.book.springtdd.infrastructure",
        "com.demo.book.springtdd.support"
})
public class ShopperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopperApplication.class, args);
    }
}

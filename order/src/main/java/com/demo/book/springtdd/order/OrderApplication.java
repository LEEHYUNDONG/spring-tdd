package com.demo.book.springtdd.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.demo.book.springtdd.order",
        "com.demo.book.springtdd.exception",
        "com.demo.book.springtdd.infrastructure",
        "com.demo.book.springtdd.support"
})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

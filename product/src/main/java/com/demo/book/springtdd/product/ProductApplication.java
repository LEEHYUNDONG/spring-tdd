package com.demo.book.springtdd.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.demo.book.springtdd.product",
        "com.demo.book.springtdd.seller",
        "com.demo.book.springtdd.exception",
        "com.demo.book.springtdd.infrastructure",
        "com.demo.book.springtdd.support"
})
@EntityScan(basePackages = {
        "com.demo.book.springtdd.product.domain",
        "com.demo.book.springtdd.seller.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.demo.book.springtdd.product.adapter.out.persistence.repository",
        "com.demo.book.springtdd.seller.adapter.out.persistence.repository"
})
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}

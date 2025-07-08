package com.demo.book.springtdd.testfixture;

import com.demo.book.springtdd.domain.ProductsRepository;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

public class TestFixtureConfiguration {

    @Bean
    @Scope("prototype")
    TestFixture testFixture(Environment environment, ProductsRepository productsRepository) {
        return TestFixture.create(environment, productsRepository);
    }
}

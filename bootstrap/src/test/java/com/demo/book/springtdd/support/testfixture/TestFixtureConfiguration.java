package com.demo.book.springtdd.support.testfixture;

import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
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

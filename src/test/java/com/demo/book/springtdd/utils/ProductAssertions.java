package com.demo.book.springtdd.utils;

import com.demo.book.springtdd.command.RegisterProductCommand;
import com.demo.book.springtdd.view.SellerProductView;
import org.assertj.core.api.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductAssertions {
    public static ThrowingConsumer<? super SellerProductView> isDerivedFrom(RegisterProductCommand command) {
        return product -> {
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount()).matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity()).isEqualTo(command.stockQunatity());

        };
    }
    private static Predicate<? super BigDecimal> equals(BigDecimal value) {
        return actual -> actual.compareTo(value) == 0;
    }

}

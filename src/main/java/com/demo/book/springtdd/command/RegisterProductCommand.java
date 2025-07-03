package com.demo.book.springtdd.command;

import java.math.BigDecimal;

public record RegisterProductCommand(
        String name,
        String imageUri,
        String description,
        BigDecimal priceAmount,
        int stockQunatity
) {

}

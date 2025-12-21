package com.demo.book.springtdd.integration.utils;

import com.demo.book.springtdd.adapter.in.dto.command.RegisterProductCommand;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterProductCommandGenerator {

    public static RegisterProductCommand generateRegisterProductCommand() {
        return new RegisterProductCommand(
                generateProductName(),
                generateProductImageUri(),
                generateProductDescription(),
                generateProductPriceAmount(),
                generateProductStockQuantity()
        );
    }
    public static RegisterProductCommand generateRegisterProductCommand(String uri) {
        return new RegisterProductCommand(
                generateProductName(),
                uri,
                generateProductDescription(),
                generateProductPriceAmount(),
                generateProductStockQuantity()
        );
    }

    private static int generateProductStockQuantity() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(10, 100);
    }

    private static BigDecimal generateProductPriceAmount() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return BigDecimal.valueOf(random.nextInt(1000, 10000));
    }

    private static String generateProductDescription() {
        return "description" + UUID.randomUUID();
    }

    private static String generateProductImageUri() {
        return "https://example.com/image/" + UUID.randomUUID() + ".jpg";
    }

    private static String generateProductName() {
        return "name" + UUID.randomUUID();
    }
}

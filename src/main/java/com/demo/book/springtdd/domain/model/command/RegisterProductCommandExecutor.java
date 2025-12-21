package com.demo.book.springtdd.domain.model.command;

import com.demo.book.springtdd.adapter.in.dto.command.RegisterProductCommand;
import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.domain.exception.InvalidCommandException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.time.ZoneOffset.UTC;

public class RegisterProductCommandExecutor {

    private final Consumer<Product> saveProduct;

    public RegisterProductCommandExecutor(Consumer<Product> saveProduct) {
        this.saveProduct = saveProduct;
    }

    public void execute(
            UUID productId,
            UUID sellerId,
            RegisterProductCommand command
    ) {
        validateCommand(command);
        Product product = createProduct(productId, sellerId, command);
        saveProduct(product);
    }


    private static Product createProduct(UUID productId, UUID sellerId, RegisterProductCommand command) {
        var product = new Product();
        product.setId(productId);
        product.setName(command.name());
        product.setSellerId(sellerId);
        product.setImageUri(command.imageUri());
        product.setDescription(command.description());
        product.setPriceAmount(command.priceAmount());
        product.setStockQuantity(command.stockQunatity());
        product.setRegisteredAt(LocalDateTime.now(UTC));
        return product;
    }

    private void saveProduct(Product product) {
        saveProduct.accept(product);
    }

    private static void validateCommand(RegisterProductCommand command) {
        if(isValidUri(command.imageUri()) == false) {
            throw new InvalidCommandException();
        }
    }

    public static boolean isValidUri(String imageUri) {
        try {
            URI uri = URI.create(imageUri);
            return uri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


}

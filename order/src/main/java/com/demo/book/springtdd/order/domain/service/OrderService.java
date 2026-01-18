package com.demo.book.springtdd.order.domain.service;

import com.demo.book.springtdd.exception.InvalidCommandException;
import com.demo.book.springtdd.order.adapter.out.grpc.ProductGrpcClient;
import com.demo.book.springtdd.order.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductGrpcClient productGrpcClient;

    public Order createOrder(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidCommandException();
        }

        var product = productGrpcClient.getProductById(productId);

        //debug
        log.debug("product {}", product);

        if (product.stockQuantity() < quantity) {
            throw new InvalidCommandException();
        }

        productGrpcClient.decreaseStock(productId, quantity);

        BigDecimal totalAmount = product.priceAmount().multiply(BigDecimal.valueOf(quantity));
        return new Order(
                UUID.randomUUID(),
                product.id(),
                product.seller().id(),
                product.name(),
                quantity,
                product.priceAmount(),
                totalAmount
        );
    }
}

package com.demo.book.springtdd.order.domain.service;

import com.demo.book.springtdd.exception.InvalidCommandException;
import com.demo.book.springtdd.order.adapter.out.grpc.ProductGrpcAdapter;
import com.demo.book.springtdd.order.domain.Order;
import com.demo.book.springtdd.order.domain.port.in.OrderUsecase;
import com.demo.book.springtdd.order.domain.port.out.ProductGrpcPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService implements OrderUsecase {

    private final ProductGrpcPort productGrpcPort;

    public Order createOrder(UUID productId, int quantity) {
        // 이후 재고 차감 adapter를 붙여야할까?
        if (quantity <= 0) {
            throw new InvalidCommandException();
        }

        var product = productGrpcPort.getProductById(productId);

        //debug
        log.debug("product {}", product);

        if (product.stockQuantity() < quantity) {
            throw new InvalidCommandException();
        }

        productGrpcPort.decreaseStock(productId, quantity);

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

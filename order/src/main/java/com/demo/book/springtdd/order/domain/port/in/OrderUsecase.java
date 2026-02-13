package com.demo.book.springtdd.order.domain.port.in;

import com.demo.book.springtdd.order.domain.Order;

import java.util.UUID;

public interface OrderUsecase {

    Order createOrder(UUID productId, int quantity);
}

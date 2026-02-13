package com.demo.book.springtdd.order.adapter.in.controller;

import com.demo.book.springtdd.order.adapter.in.dto.CreateOrderRequest;
import com.demo.book.springtdd.order.adapter.in.dto.OrderView;
import com.demo.book.springtdd.order.domain.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    OrderView createOrder(@RequestBody CreateOrderRequest request) {
        var order = orderService.createOrder(request.productId(), request.quantity());
        return new OrderView(
                order.id(),
                order.productId(),
                order.sellerId(),
                order.productName(),
                order.quantity(),
                order.unitPrice(),
                order.totalAmount()
        );
    }
}

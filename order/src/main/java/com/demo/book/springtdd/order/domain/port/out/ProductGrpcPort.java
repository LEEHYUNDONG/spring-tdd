package com.demo.book.springtdd.order.domain.port.out;

import com.demo.book.springtdd.order.adapter.out.grpc.dto.ProductDto;

import java.util.UUID;

// 우선 query, command 합친다..
public interface ProductGrpcPort {
    ProductDto getProductById(UUID productId);

    ProductDto getProductBySellerId(UUID sellerId);

    void decreaseStock(UUID productId, int quantity);
}

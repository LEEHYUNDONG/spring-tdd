package com.demo.book.springtdd.seller.application.port.in.query;

import java.util.UUID;

public record ReadSellerQuery(UUID sellerId) {
}

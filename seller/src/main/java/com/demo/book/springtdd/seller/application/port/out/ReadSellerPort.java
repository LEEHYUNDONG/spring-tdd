package com.demo.book.springtdd.seller.application.port.out;

import com.demo.book.springtdd.seller.domain.Seller;

import java.util.UUID;

public interface ReadSellerPort {
    Seller findByEmail(String email);
    Seller findById(UUID id);
}

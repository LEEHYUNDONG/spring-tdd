package com.demo.book.springtdd.application.port.out;

import com.demo.book.springtdd.domain.Shopper;

import java.util.UUID;

public interface ReadShopperPort {
    Shopper findByEmail(String email);
    Shopper findById(UUID id);
}
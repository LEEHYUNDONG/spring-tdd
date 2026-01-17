package com.demo.book.springtdd.shopper.application.port.out;

import com.demo.book.springtdd.shopper.domain.Shopper;

import java.util.UUID;

public interface ReadShopperPort {
    Shopper findByEmail(String email);
    Shopper findById(UUID id);
}

package com.demo.book.springtdd.shopper.application.port.out;

import com.demo.book.springtdd.shopper.domain.Shopper;

import java.util.Optional;
import java.util.UUID;

public interface ReadShopperPort {
    Shopper findByEmail(String email);
    Optional<Shopper> findById(UUID id);
}

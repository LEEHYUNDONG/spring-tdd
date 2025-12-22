package com.demo.book.springtdd.adapter.out.persistence;

import com.demo.book.springtdd.adapter.out.persistence.repository.ShopperRepository;
import com.demo.book.springtdd.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.domain.Shopper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopperQueryAdapter implements ReadShopperPort {

    private final ShopperRepository shopperRepository;

    @Override
    public Shopper findByEmail(String email) {
        return shopperRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Shopper findById(UUID id) {
        return shopperRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
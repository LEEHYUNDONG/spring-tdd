package com.demo.book.springtdd.shopper.adapter.out.persistence;

import com.demo.book.springtdd.shopper.adapter.out.persistence.entity.ShopperJpaEntity;
import com.demo.book.springtdd.shopper.adapter.out.persistence.repository.JpaShopperRepository;
import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopperQueryAdapter implements ReadShopperPort {

    private final JpaShopperRepository jpaShopperRepository;

    @Override
    public Shopper findByEmail(String email) {
        return jpaShopperRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new)
                .toDomain();
    }

    @Override
    public Shopper findById(UUID id) {
        return jpaShopperRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new)
                .toDomain();
    }
}

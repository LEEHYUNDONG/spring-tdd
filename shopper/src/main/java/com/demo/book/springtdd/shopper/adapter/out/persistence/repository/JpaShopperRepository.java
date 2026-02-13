package com.demo.book.springtdd.shopper.adapter.out.persistence.repository;

import com.demo.book.springtdd.shopper.adapter.out.persistence.entity.ShopperJpaEntity;
import com.demo.book.springtdd.shopper.domain.Shopper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaShopperRepository extends JpaRepository<ShopperJpaEntity, Long> {
    Optional<ShopperJpaEntity> findByEmail(String email);

    Optional<ShopperJpaEntity> findByUsername(String username);

    Optional<ShopperJpaEntity> findById(UUID id);
}

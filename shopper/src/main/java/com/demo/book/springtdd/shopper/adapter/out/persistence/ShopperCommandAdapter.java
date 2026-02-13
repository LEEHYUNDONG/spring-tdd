package com.demo.book.springtdd.shopper.adapter.out.persistence;

import com.demo.book.springtdd.shopper.adapter.out.persistence.entity.ShopperJpaEntity;
import com.demo.book.springtdd.shopper.adapter.out.persistence.repository.JpaShopperRepository;
import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopperCommandAdapter implements CreateShopperPort {

    private final JpaShopperRepository jpaShopperRepository;

    @Override
    public void create(Shopper shopper) {
        ShopperJpaEntity entity = ShopperJpaEntity.from(shopper);
        jpaShopperRepository.save(entity);
    }
}

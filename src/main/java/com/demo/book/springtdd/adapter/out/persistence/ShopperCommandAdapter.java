package com.demo.book.springtdd.adapter.out.persistence;

import com.demo.book.springtdd.adapter.out.persistence.repository.ShopperRepository;
import com.demo.book.springtdd.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.domain.Shopper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopperCommandAdapter implements CreateShopperPort {

    private final ShopperRepository shopperRepository;

    @Override
    public void create(Shopper shopper) {
        shopperRepository.save(shopper);
    }
}

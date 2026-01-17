package com.demo.book.springtdd.shopper.adapter.out.persistence;

import com.demo.book.springtdd.shopper.adapter.out.persistence.repository.ShopperRepository;
import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.shopper.domain.Shopper;
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

package com.demo.book.springtdd.shopper.application.port.out;

import com.demo.book.springtdd.shopper.domain.Shopper;

public interface CreateShopperPort {

    void create(Shopper shopper);
}

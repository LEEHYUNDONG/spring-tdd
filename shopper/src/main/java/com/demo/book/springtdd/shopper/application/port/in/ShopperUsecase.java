package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.domain.*;

import java.util.UUID;

public interface ShopperUsecase {

    ShopperId signUp(CreateShopperCommand command);

    ShopperInfo read(ReadShopperQuery query);
}

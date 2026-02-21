package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;
import com.demo.book.springtdd.shopper.domain.ReadShopperQuery;
import com.demo.book.springtdd.shopper.domain.Shopper;
import com.demo.book.springtdd.shopper.domain.ShopperInfo;

public interface ShopperUsecase {

    Shopper signUp(CreateShopperCommand command);

    ShopperInfo read(ReadShopperQuery query);
}

package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.domain.ReadShopperQuery;
import com.demo.book.springtdd.shopper.domain.ShopperInfo;

public interface ForReadingShopper {

    ShopperInfo read(ReadShopperQuery query);
}

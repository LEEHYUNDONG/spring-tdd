package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.application.port.in.query.ReadShopperQuery;
import com.demo.book.springtdd.shopper.application.port.in.result.ShopperInfo;

public interface ForReadingShopper {

    ShopperInfo read(ReadShopperQuery query);
}

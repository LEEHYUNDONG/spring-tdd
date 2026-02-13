package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;

public interface ForCreatingShopper {

    void signUp(CreateShopperCommand command);
}

package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.application.port.in.command.CreateShopperCommand;

public interface ForCreatingShopper {

    void signUp(CreateShopperCommand command);
}

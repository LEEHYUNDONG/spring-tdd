package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.adapter.in.dto.command.CreateShopperCommand;

public interface ForCreatingShopper {

    void signUp(CreateShopperCommand command);
}

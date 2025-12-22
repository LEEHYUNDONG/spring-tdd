package com.demo.book.springtdd.application.port.in;

import com.demo.book.springtdd.adapter.in.dto.command.CreateShopperCommand;

public interface ForCreatingShopper {

    void signUp(CreateShopperCommand command);
}
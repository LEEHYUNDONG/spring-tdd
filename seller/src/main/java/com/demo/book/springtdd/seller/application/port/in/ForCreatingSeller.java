package com.demo.book.springtdd.seller.application.port.in;

import com.demo.book.springtdd.seller.adapter.in.dto.command.CreateSellerCommand;

public interface ForCreatingSeller {

    void signUp(CreateSellerCommand seller);
}

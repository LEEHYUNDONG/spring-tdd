package com.demo.book.springtdd.application.port.in;

import com.demo.book.springtdd.adapter.in.dto.command.CreateSellerCommand;
import com.demo.book.springtdd.domain.Seller;

public interface ForCreatingSeller {

    void signUp(CreateSellerCommand seller);
}

package com.demo.book.springtdd.application.port.out;

import com.demo.book.springtdd.domain.Seller;

public interface CreateSellerPort {

    void create(Seller seller);
}

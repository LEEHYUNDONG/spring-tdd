package com.demo.book.springtdd.seller.application.port.out;

import com.demo.book.springtdd.seller.domain.Seller;

public interface CreateSellerPort {

    void create(Seller seller);
}

package com.demo.book.springtdd.seller.application.port.in;

import com.demo.book.springtdd.seller.application.port.in.query.ReadSellerQuery;
import com.demo.book.springtdd.seller.application.port.in.result.SellerInfo;

public interface ForReadingSeller {

    SellerInfo read(ReadSellerQuery query);
}

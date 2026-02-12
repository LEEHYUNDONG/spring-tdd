package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.application.port.in.query.IssueShopperTokenQuery;

public interface ForIssuingShopperToken {

    String issueToken(IssueShopperTokenQuery query);
}

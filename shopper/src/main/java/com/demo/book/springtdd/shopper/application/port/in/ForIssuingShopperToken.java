package com.demo.book.springtdd.shopper.application.port.in;

import com.demo.book.springtdd.shopper.adapter.in.dto.query.IssueShopperToken;

public interface ForIssuingShopperToken {

    String issueToken(IssueShopperToken query);
}

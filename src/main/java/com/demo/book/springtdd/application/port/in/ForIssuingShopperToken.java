package com.demo.book.springtdd.application.port.in;

import com.demo.book.springtdd.adapter.in.dto.query.IssueShopperToken;

public interface ForIssuingShopperToken {

    String issueToken(IssueShopperToken query);
}
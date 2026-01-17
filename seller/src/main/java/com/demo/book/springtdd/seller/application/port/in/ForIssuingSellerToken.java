package com.demo.book.springtdd.seller.application.port.in;

import com.demo.book.springtdd.seller.adapter.in.dto.query.IssueSellerToken;

public interface ForIssuingSellerToken {

    String issueToken(IssueSellerToken query);
}

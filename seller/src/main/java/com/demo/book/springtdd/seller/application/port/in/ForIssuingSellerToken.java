package com.demo.book.springtdd.seller.application.port.in;

import com.demo.book.springtdd.seller.application.port.in.query.IssueSellerTokenQuery;

public interface ForIssuingSellerToken {

    String issueToken(IssueSellerTokenQuery query);
}

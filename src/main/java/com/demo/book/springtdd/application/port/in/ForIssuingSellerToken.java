package com.demo.book.springtdd.application.port.in;

import com.demo.book.springtdd.adapter.in.dto.query.IssueSellerToken;

public interface ForIssuingSellerToken {

    String issueToken(IssueSellerToken query);
}

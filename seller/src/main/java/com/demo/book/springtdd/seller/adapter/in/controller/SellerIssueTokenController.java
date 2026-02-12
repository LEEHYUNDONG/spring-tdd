package com.demo.book.springtdd.seller.adapter.in.controller;

import com.demo.book.springtdd.seller.adapter.in.dto.request.IssueSellerTokenRequest;
import com.demo.book.springtdd.seller.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.seller.application.port.in.ForIssuingSellerToken;
import com.demo.book.springtdd.seller.application.port.in.query.IssueSellerTokenQuery;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SellerIssueTokenController {

    private final ForIssuingSellerToken forIssuingSellerToken;

    @PostMapping("/seller/issueToken")
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerTokenRequest request) {
        try {
            var query = new IssueSellerTokenQuery(request.email(), request.password());
            String token = forIssuingSellerToken.issueToken(query);
            return ResponseEntity.ok(new AccessTokenCarrier(token));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

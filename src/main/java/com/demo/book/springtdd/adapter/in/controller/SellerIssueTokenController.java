package com.demo.book.springtdd.adapter.in.controller;

import com.demo.book.springtdd.adapter.in.dto.query.IssueSellerToken;
import com.demo.book.springtdd.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.application.port.in.ForIssuingSellerToken;
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
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerToken issueSellerToken) {
        try {
            String token = forIssuingSellerToken.issueToken(issueSellerToken);
            return ResponseEntity.ok(new AccessTokenCarrier(token));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

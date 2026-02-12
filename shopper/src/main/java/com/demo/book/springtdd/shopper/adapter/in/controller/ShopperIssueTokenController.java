package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.adapter.in.dto.request.IssueShopperTokenRequest;
import com.demo.book.springtdd.shopper.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.shopper.application.port.in.ForIssuingShopperToken;
import com.demo.book.springtdd.shopper.application.port.in.query.IssueShopperTokenQuery;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShopperIssueTokenController {

    private final ForIssuingShopperToken forIssuingShopperToken;

    @PostMapping("/shopper/issueToken")
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueShopperTokenRequest request) {
        try {
            var query = new IssueShopperTokenQuery(request.email(), request.password());
            String token = forIssuingShopperToken.issueToken(query);
            return ResponseEntity.ok(new AccessTokenCarrier(token));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

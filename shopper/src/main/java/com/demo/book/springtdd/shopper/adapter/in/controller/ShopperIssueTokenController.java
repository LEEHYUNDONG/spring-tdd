package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.adapter.in.dto.query.IssueShopperToken;
import com.demo.book.springtdd.shopper.adapter.in.dto.result.AccessTokenCarrier;
import com.demo.book.springtdd.shopper.application.port.in.ForIssuingShopperToken;
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
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueShopperToken issueShopperToken) {
        try {
            String token = forIssuingShopperToken.issueToken(issueShopperToken);
            return ResponseEntity.ok(new AccessTokenCarrier(token));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

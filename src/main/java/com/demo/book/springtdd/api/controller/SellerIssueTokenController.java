package com.demo.book.springtdd.api.controller;


import com.demo.book.springtdd.config.JwtKeyHolder;
import com.demo.book.springtdd.domain.Seller;
import com.demo.book.springtdd.domain.SellerRepository;
import com.demo.book.springtdd.query.IssueSellerToken;
import com.demo.book.springtdd.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

@RestController
public record SellerIssueTokenController(
        JwtKeyHolder jwtKeyHolder,
        PasswordEncoder passwordEncoder,
        SellerRepository sellerRepository
) {

    @PostMapping("/seller/issueToken")
    public ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerToken issueSellerToken) {
        return sellerRepository.findByEmail(issueSellerToken.email())
                .filter(seller -> passwordEncoder.matches(
                        issueSellerToken.password(),
                        seller.getHashedPassword()))
                .map(seller -> composeToken())
                .map(AccessTokenCarrier::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
                .builder()
                .signWith(jwtKeyHolder.secretKey()).compact();
    }
}

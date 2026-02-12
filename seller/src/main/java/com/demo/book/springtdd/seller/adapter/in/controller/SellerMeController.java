package com.demo.book.springtdd.seller.adapter.in.controller;

import com.demo.book.springtdd.seller.adapter.in.dto.view.SellerMeView;
import com.demo.book.springtdd.seller.application.port.in.ForReadingSeller;
import com.demo.book.springtdd.seller.application.port.in.query.ReadSellerQuery;
import com.demo.book.springtdd.seller.application.port.in.result.SellerInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController(ForReadingSeller forReadingSeller) {

    @GetMapping("/seller/me")
    SellerMeView sellerMe(Principal user) {
        UUID id = UUID.fromString(user.getName());
        SellerInfo sellerInfo = forReadingSeller.read(new ReadSellerQuery(id));
        return new SellerMeView(
                sellerInfo.id(),
                sellerInfo.email(),
                sellerInfo.username(),
                sellerInfo.contactEmail()
        );
    }

}

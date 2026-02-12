package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.adapter.in.dto.view.ShopperMeView;
import com.demo.book.springtdd.shopper.application.port.in.ForReadingShopper;
import com.demo.book.springtdd.shopper.application.port.in.query.ReadShopperQuery;
import com.demo.book.springtdd.shopper.application.port.in.result.ShopperInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeViewController(ForReadingShopper forReadingShopper) {

    @GetMapping("/shopper/me")
    public ShopperMeView me(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        ShopperInfo shopperInfo = forReadingShopper.read(new ReadShopperQuery(id));
        return new ShopperMeView(shopperInfo.id(), shopperInfo.email(), shopperInfo.username());
    }

}

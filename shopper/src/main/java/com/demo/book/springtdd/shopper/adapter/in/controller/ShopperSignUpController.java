package com.demo.book.springtdd.shopper.adapter.in.controller;

import com.demo.book.springtdd.shopper.adapter.in.dto.request.CreateShopperRequest;
import com.demo.book.springtdd.shopper.adapter.in.dto.view.ShopperMeView;
import com.demo.book.springtdd.shopper.application.port.in.ShopperUsecase;
import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;
import com.demo.book.springtdd.shopper.domain.ReadShopperQuery;
import com.demo.book.springtdd.shopper.domain.ShopperInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static com.demo.book.springtdd.support.UserPropertyValidator.*;

@RestController
@RequiredArgsConstructor
public class ShopperSignUpController {

    private final ShopperUsecase shopperUsecase;

    @PostMapping("/shopper/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateShopperRequest request) {
        if (isRequestNotValid(request)) {
            return ResponseEntity.badRequest().build();
        }

        var command = new CreateShopperCommand(
                request.email(),
                request.username(),
                request.password()
        );
        shopperUsecase.signUp(command);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shopper/me")
    public ShopperMeView me(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        System.out.println("**id = " + id);
        ShopperInfo shopperInfo = shopperUsecase.read(new ReadShopperQuery(id));
        return new ShopperMeView(shopperInfo.id(), shopperInfo.email(), shopperInfo.username());
    }

    public static boolean isRequestNotValid(CreateShopperRequest request) {
        return !isEmailValid(request.email()) ||
                !isUsernameValid(request.username()) ||
                !isPasswordValid(request.password());
    }
}

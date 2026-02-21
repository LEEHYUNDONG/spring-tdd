package com.demo.book.springtdd.shopper.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@EqualsAndHashCode
public class Shopper {

    private ShopperId id;
    private String email;
    private String username;
    private String hashedPassword;


    public static Shopper register(CreateShopperCommand command, PasswordEncoder passwordEncoder) {

        Shopper shopper = new Shopper();
        shopper.setId(ShopperId.generate());
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));

        return shopper;
    }


}

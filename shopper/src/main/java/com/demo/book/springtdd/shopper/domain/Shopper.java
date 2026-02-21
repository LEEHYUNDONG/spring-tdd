package com.demo.book.springtdd.shopper.domain;

import com.demo.book.springtdd.shopper.adapter.out.persistence.entity.ShopperJpaEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
public class Shopper {

    private UUID id;
    private String email;
    private String username;
    private String hashedPassword;


    public static Shopper register(CreateShopperCommand command, PasswordEncoder passwordEncoder) {

        Shopper shopper = new Shopper();
        shopper.setId(UUID.randomUUID());
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));

        return shopper;
    }

    public Shopper toDomain(ShopperJpaEntity jpaEntity) {
        Shopper shopper = new Shopper();
        shopper.setUsername(jpaEntity.getUsername());
        shopper.setEmail(jpaEntity.getEmail());
        shopper.setHashedPassword(jpaEntity.getHashedPassword());
        return shopper;
    }

}

package com.demo.book.springtdd.shopper.adapter.out.persistence.entity;

import com.demo.book.springtdd.shopper.domain.Shopper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@Table(name = "shopper")
public class ShopperJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String hashedPassword;

    public static ShopperJpaEntity from(Shopper shopper) {
        ShopperJpaEntity entity = new ShopperJpaEntity();
        entity.setId(shopper.getId());
        entity.setEmail(shopper.getEmail());
        entity.setUsername(shopper.getUsername());
        entity.setHashedPassword(shopper.getHashedPassword());
        return entity;
    }

    public Shopper toDomain() {
        Shopper shopper = new Shopper();
        shopper.setId(this.id);
        shopper.setEmail(this.email);
        shopper.setUsername(this.username);
        shopper.setHashedPassword(this.hashedPassword);
        return shopper;
    }

}

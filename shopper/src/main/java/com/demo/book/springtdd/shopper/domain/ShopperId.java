package com.demo.book.springtdd.shopper.domain;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShopperId {

    private UUID value;

    protected ShopperId() {}

    public ShopperId(UUID value) {
        this.value = value;
    }

    public static ShopperId generate() {
        return new ShopperId(UUID.randomUUID());
    }


}

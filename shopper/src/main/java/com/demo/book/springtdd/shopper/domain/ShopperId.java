package com.demo.book.springtdd.shopper.domain;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ShopperId {

    @EqualsAndHashCode.Include
    private UUID value;

    protected ShopperId() {}

    public ShopperId(UUID value) {
        this.value = value;
    }

    public static ShopperId generate() {
        return new ShopperId(UUID.randomUUID());
    }

//    @Override
//    public String toString() {
//        return value.toString();
//    }

}

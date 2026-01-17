package com.demo.book.springtdd.seller.adapter.in.dto.view;

import java.util.UUID;

public record SellerView(
        UUID id,
        String username,
        String contactEmail

) {

}

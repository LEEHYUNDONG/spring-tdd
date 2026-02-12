package com.demo.book.springtdd.seller.application.port.in.command;

import java.util.UUID;

public record ChangeContactEmailCommand(
        UUID sellerId,
        String contactEmail
) {
}

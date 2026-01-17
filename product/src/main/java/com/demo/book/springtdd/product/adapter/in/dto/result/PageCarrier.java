package com.demo.book.springtdd.product.adapter.in.dto.result;

public record PageCarrier<T>(T[] items, String contunuationToken) {
}

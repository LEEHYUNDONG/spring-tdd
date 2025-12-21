package com.demo.book.springtdd.adapter.in.dto.result;

public record PageCarrier<T>(T[] items, String contunuationToken) {
}

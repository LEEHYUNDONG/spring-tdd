package com.demo.book.springtdd.result;

public record PageCarrier<T>(T[] items, String contunuationToken) {
}

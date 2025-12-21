package com.demo.book.springtdd.adapter.out.persistence;

import com.demo.book.springtdd.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.application.port.out.CreateSellerPort;
import com.demo.book.springtdd.domain.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerCommandAdapter implements CreateSellerPort {
    private final SellerRepository sellerRepository;

    @Override
    public void create(Seller seller) {
        sellerRepository.save(seller);
    }
}

package com.demo.book.springtdd.seller.adapter.out.persistence;

import com.demo.book.springtdd.seller.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.seller.application.port.out.CreateSellerPort;
import com.demo.book.springtdd.seller.domain.Seller;
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

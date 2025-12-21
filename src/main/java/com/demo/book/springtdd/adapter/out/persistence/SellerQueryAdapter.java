package com.demo.book.springtdd.adapter.out.persistence;

import com.demo.book.springtdd.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.application.port.out.ReadSellerPort;
import com.demo.book.springtdd.domain.Seller;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SellerQueryAdapter implements ReadSellerPort {

    private final SellerRepository sellerRepository;

    @Override
    public Seller findByEmail(String email) {
        return sellerRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Seller findById(UUID id) {
        return sellerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}

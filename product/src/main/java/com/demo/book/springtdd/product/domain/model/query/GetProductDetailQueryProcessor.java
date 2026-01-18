package com.demo.book.springtdd.product.domain.model.query;

import com.demo.book.springtdd.product.adapter.in.dto.view.ProductView;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

public class GetProductDetailQueryProcessor {

    private final EntityManager entityManager;

    public GetProductDetailQueryProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<ProductView> process(UUID productId) {
        String queryString = """
                SELECT new com.demo.book.springtdd.product.domain.model.query.ProductSellerTuple(p, s)
                FROM Product p
                JOIN Seller s ON p.sellerId = s.id
                WHERE p.id = :productId
                """;

        return entityManager
                .createQuery(queryString, ProductSellerTuple.class)
                .setParameter("productId", productId)
                .getResultList()
                .stream()
                .findFirst()
                .map(ProductSellerTuple::toView);
    }
}

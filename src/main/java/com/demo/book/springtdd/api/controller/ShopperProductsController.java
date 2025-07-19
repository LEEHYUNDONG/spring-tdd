package com.demo.book.springtdd.api.controller;

import com.demo.book.springtdd.api.ProductSellerTuple;
import com.demo.book.springtdd.domain.ProductsRepository;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.view.ProductView;

import jakarta.persistence.EntityManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public record ShopperProductsController(
    ProductsRepository productsRepository,
    EntityManager em
) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(
            @RequestParam(required = false) String continuationToken
    ) {
        String queryString = """
                SELECT new com.demo.book.springtdd.api.ProductSellerTuple(p, s)
                FROM Product p
                JOIN Seller s ON p.sellerId = s.id
                WHERE :cursor IS NULL OR p.dataKey <= :cursor
                ORDER BY p.dataKey DESC
                """;
        int pageSize = 10;

        List<ProductSellerTuple> results = em
                .createQuery(queryString, ProductSellerTuple.class)
                .setParameter("cursor", decodeCursor(continuationToken))
                .setMaxResults(pageSize + 1)
                .getResultList();

        ProductView[] item = results
                .stream()
                .limit(pageSize)
                .map(ProductSellerTuple::toView)
                .toArray(ProductView[]::new);

        Long next = null;
        if(!ObjectUtils.isEmpty(results)) {
            next = results.size() <= pageSize ? null :
                    results.getLast().product().getDataKey();
        }

        return new PageCarrier<>(item, encodeCursor(next));
    }

    private Long decodeCursor(String continuationToken) {
        if(StringUtils.isEmpty(continuationToken)){
            return null;
        }
        byte[] data = Base64.getDecoder().decode(continuationToken);
        return Long.parseLong(new String(data, UTF_8));
    }

    private String encodeCursor(Long cursor) {
        if (cursor == null) {
            return null;
        }
        byte[] data = cursor.toString().getBytes(UTF_8);
        return Base64.getEncoder().encodeToString(data);
    }


}

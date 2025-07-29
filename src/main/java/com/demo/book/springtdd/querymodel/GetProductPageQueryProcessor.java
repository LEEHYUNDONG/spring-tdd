package com.demo.book.springtdd.querymodel;

import com.demo.book.springtdd.query.GetProductPage;
import com.demo.book.springtdd.result.PageCarrier;
import com.demo.book.springtdd.view.ProductView;
import jakarta.persistence.EntityManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GetProductPageQueryProcessor {
    private final EntityManager entityManager;

    public GetProductPageQueryProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PageCarrier<ProductView> process(GetProductPage query) {
        String queryString = """
                SELECT new com.demo.book.springtdd.querymodel.ProductSellerTuple(p, s)
                FROM Product p
                JOIN Seller s ON p.sellerId = s.id
                WHERE :cursor IS NULL OR p.dataKey <= :cursor
                ORDER BY p.dataKey DESC
                """;
        int pageSize = 10;

        List<ProductSellerTuple> results = entityManager
                .createQuery(queryString, ProductSellerTuple.class)
                .setParameter("cursor", decodeCursor(query.continuationToken()))
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

    private static Long decodeCursor(String continuationToken) {
        if(StringUtils.isEmpty(continuationToken)){
            return null;
        }
        byte[] data = Base64.getDecoder().decode(continuationToken);
        return Long.parseLong(new String(data, UTF_8));
    }

    private static String encodeCursor(Long cursor) {
        if (cursor == null) {
            return null;
        }
        byte[] data = cursor.toString().getBytes(UTF_8);
        return Base64.getEncoder().encodeToString(data);
    }

}

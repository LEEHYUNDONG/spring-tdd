package com.demo.book.springtdd.querymodel;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.query.FindSellerProduct;
import com.demo.book.springtdd.view.SellerProductView;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class FindSellerProoductQueryProcessor {

    private final Function<UUID, Optional<Product>> findProduct;

    public FindSellerProoductQueryProcessor(
            Function<UUID, Optional<Product>> findProduct
    ) {
        this.findProduct = findProduct;
    }


    public Optional<SellerProductView> process(FindSellerProduct query) {
        return this.findProduct
                .apply(query.productId())
                .filter(product -> product.getSellerId().equals(query.sellerId()))
                .map(ProductMapper::convertToView);
    }
}

package com.demo.book.springtdd.querymodel;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.query.GetSellerProducts;
import com.demo.book.springtdd.view.ArrayCarrier;
import com.demo.book.springtdd.view.SellerProductView;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class GetSellerProductsQueryProcessor {

    public final Function<UUID, List<Product>> getProductsOfSeller;

    public GetSellerProductsQueryProcessor(
            Function<UUID, List<Product>> getProductsOfSeller
    ) {
        this.getProductsOfSeller = getProductsOfSeller;
    }

    public ArrayCarrier<SellerProductView> process(GetSellerProducts query) {
        SellerProductView[] items = this.getProductsOfSeller.apply(query.sellerId())
                .stream()
                .map(ProductMapper::convertToView)
                .sorted(Comparator.comparing(SellerProductView::registeredAt).reversed())
                .toArray(SellerProductView[]::new);

        ArrayCarrier<SellerProductView> results = new ArrayCarrier<>(items);
        return results;
    }
}

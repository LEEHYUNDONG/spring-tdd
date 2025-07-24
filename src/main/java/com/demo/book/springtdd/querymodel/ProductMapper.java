package com.demo.book.springtdd.querymodel;

import com.demo.book.springtdd.domain.Product;
import com.demo.book.springtdd.view.SellerProductView;

public class ProductMapper {

    public static SellerProductView convertToView(Product product) {
        return new SellerProductView(
                product.getId(),
                product.getName(),
                product.getImageUri(),
                product.getDescription(),
                product.getPriceAmount(),
                product.getStockQuantity(),
                product.getRegisteredAt()
        );
    }
}

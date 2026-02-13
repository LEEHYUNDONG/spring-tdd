package com.demo.book.springtdd.order.adapter.out.grpc;

import com.demo.book.springtdd.order.adapter.out.grpc.dto.ProductDto;
import com.demo.book.springtdd.order.adapter.out.grpc.dto.SellerDto;
import com.demo.book.springtdd.order.adapter.out.grpc.proto.DecreaseStockRequest;
import com.demo.book.springtdd.order.adapter.out.grpc.proto.ProductId;
import com.demo.book.springtdd.order.adapter.out.grpc.proto.ProductServiceGrpc;
import com.demo.book.springtdd.order.adapter.out.grpc.proto.ProductView;
import com.demo.book.springtdd.order.adapter.out.grpc.proto.SellerId;
import com.demo.book.springtdd.order.domain.port.out.ProductGrpcPort;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ProductGrpcAdapter implements ProductGrpcPort {

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    @Override
    public ProductDto getProductById(UUID productId) {
        ProductId request = ProductId.newBuilder()
                .setProductId(productId.toString())
                .build();

        ProductView response = productServiceStub.findProductById(request);

        return mapToProductDto(response);
    }

    @Override
    public ProductDto getProductBySellerId(UUID sellerId) {
        SellerId request = SellerId.newBuilder()
                .setSellerId(sellerId.toString())
                .build();

        ProductView response = productServiceStub.findProductBySellerId(request);

        return mapToProductDto(response);
    }

    @Override
    public void decreaseStock(UUID productId, int quantity) {
        DecreaseStockRequest request = DecreaseStockRequest.newBuilder()
                .setProductId(productId.toString())
                .setQuantity(quantity)
                .build();

        productServiceStub.decreaseStock(request);
    }

    private ProductDto mapToProductDto(ProductView view) {
        return new ProductDto(
                UUID.fromString(view.getId()),
                new SellerDto(
                        UUID.fromString(view.getSeller().getId()),
                        view.getSeller().getUsername(),
                        view.getSeller().getContactEmail()
                ),
                view.getName(),
                view.getImageUri(),
                view.getDescription(),
                BigDecimal.valueOf(view.getPriceAmount()),
                view.getStockQuantity()
        );
    }
}

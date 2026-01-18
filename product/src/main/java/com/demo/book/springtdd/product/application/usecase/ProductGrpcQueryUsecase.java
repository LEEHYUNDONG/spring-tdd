package com.demo.book.springtdd.product.application.usecase;

import com.demo.book.springtdd.product.adapter.out.persistence.repository.ProductsRepository;
import com.demo.book.springtdd.product.application.DecreaseStockRequest;
import com.demo.book.springtdd.product.application.DecreaseStockResponse;
import com.demo.book.springtdd.product.application.ProductId;
import com.demo.book.springtdd.product.application.ProductServiceGrpc;
import com.demo.book.springtdd.product.application.ProductView;
import com.demo.book.springtdd.product.application.SellerId;
import com.demo.book.springtdd.product.domain.Product;
import com.demo.book.springtdd.product.domain.mapper.ProductMapper;
import com.demo.book.springtdd.seller.adapter.out.persistence.repository.SellerRepository;
import com.demo.book.springtdd.seller.domain.Seller;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@GrpcService
public class ProductGrpcQueryUsecase extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductsRepository productsRepository;
    private final SellerRepository sellerRepository;

    @Override
    public void findProductById(ProductId request, StreamObserver<ProductView> responseObserver) {
        String productIdStr = request.getProductId();

        if (!isValidUuid(productIdStr)) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format")
                            .asRuntimeException()
            );
            return;
        }

        UUID productId = UUID.fromString(productIdStr);
        Product product = productsRepository.findById(productId)
                .orElse(null);

        if (product == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Product not found")
                            .asRuntimeException()
            );
            return;
        }

        Seller seller = sellerRepository.findById(product.getSellerId())
                .orElse(null);

        if (seller == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Seller not found")
                            .asRuntimeException()
            );
            return;
        }

        responseObserver.onNext(ProductMapper.toProto(product, seller));
        responseObserver.onCompleted();
    }

    @Override
    public void findProductBySellerId(SellerId request, StreamObserver<ProductView> responseObserver) {
        String sellerIdStr = request.getSellerId();

        if (!isValidUuid(sellerIdStr)) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format")
                            .asRuntimeException()
            );
            return;
        }

        UUID sellerId = UUID.fromString(sellerIdStr);
        var products = productsRepository.findBySellerId(sellerId);

        if (products.isEmpty()) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("No products found for seller")
                            .asRuntimeException()
            );
            return;
        }

        Seller seller = sellerRepository.findById(sellerId)
                .orElse(null);

        if (seller == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Seller not found")
                            .asRuntimeException()
            );
            return;
        }

        Product product = products.get(0);
        responseObserver.onNext(ProductMapper.toProto(product, seller));
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void decreaseStock(DecreaseStockRequest request, StreamObserver<DecreaseStockResponse> responseObserver) {
        String productIdStr = request.getProductId();
        int quantity = request.getQuantity();

        if (!isValidUuid(productIdStr)) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format")
                            .asRuntimeException()
            );
            return;
        }

        if (quantity <= 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Quantity must be positive")
                            .asRuntimeException()
            );
            return;
        }

        UUID productId = UUID.fromString(productIdStr);
        Product product = productsRepository.findById(productId)
                .orElse(null);

        if (product == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Product not found")
                            .asRuntimeException()
            );
            return;
        }

        if (product.getStockQuantity() < quantity) {
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription("Insufficient stock")
                            .asRuntimeException()
            );
            return;
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productsRepository.save(product);

        responseObserver.onNext(DecreaseStockResponse.newBuilder()
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    private boolean isValidUuid(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

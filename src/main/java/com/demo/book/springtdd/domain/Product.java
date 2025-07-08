package com.demo.book.springtdd.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(indexes = @Index(columnList = "sellerId"))
public class Product {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true, name = "product_id")
    private UUID id;

    @Column
    private UUID sellerId;

    @Column
    private String name;

    @Column(unique = true)
    private String imageUri;

    @Column(length = 1000)
    private String description;

    @Column
    private BigDecimal priceAmount;

    @Column
    private int stockQuantity;

    private LocalDateTime registeredAt;

}
